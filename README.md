# EduMetrics API

EduMetrics é uma API RESTful para acompanhamento de desempenho acadêmico de alunos do IFTM. O sistema combina persistência com Spring Data JPA, cache com Redis, mensageria com RabbitMQ e estruturas de dados customizadas para otimização: `LRUCache`, `Trie` e `RateLimiter`.

## Tecnologias

- Java 21
- Spring Boot 3.5.16
- Spring Web
- Spring Data JPA + H2 (memória)
- Spring Cache + Redis
- Spring AMQP + RabbitMQ
- JMH para benchmark
- Maven Wrapper (`mvnw.cmd`)
- Docker Compose para Redis e RabbitMQ

## Arquitetura

- `Aluno`, `Disciplina` e `Desempenho` são entidades JPA.
- `AlunoService` gerencia CRUD de alunos e usa cache para leituras frequentes.
- `AutocompletarService` indexa nomes de disciplinas em uma `Trie` para busca eficiente por prefixo.
- `DesempenhoService` registra notas, calcula média e publica eventos de geração de relatório via RabbitMQ.
- `RankingService` gera ranking de alunos com base no desempenho e utiliza cache para resultados top-K.
- `RateLimiter` controla requisições por cliente em janela deslizante.

## Componentes de desempenho

- `LRUCache`: cache local com política de remoção do elemento menos recentemente usado.
- `Trie`: autocompletar de disciplinas com busca por prefixo.
- `RateLimiter`: limite de requisições em janela de tempo usando `ConcurrentHashMap` + `ArrayDeque`.

## Como executar

### Pré-requisitos

- Java 21 instalado
- Docker (para Redis e RabbitMQ)

### Subir dependências

```bash
cd c:\Users\pedro\projeto_final_estrutura_de_dados
docker-compose up -d
```

### Executar aplicação

```bash
cd c:\Users\pedro\projeto_final_estrutura_de_dados\edumetrics
.\mvnw.cmd spring-boot:run
```

### Executar testes

```bash
cd c:\Users\pedro\projeto_final_estrutura_de_dados\edumetrics
.\mvnw.cmd test
```

### Executar benchmark JMH

```bash
cd c:\Users\pedro\projeto_final_estrutura_de_dados\edumetrics
.\mvnw.cmd package
java -jar target/benchmarks.jar
```

Para rodar benchmark específico:

```bash
java -jar target/benchmarks.jar EduMetricsBenchmark.buscaLRUCache
```

## Endpoints principais

- `POST /api/alunos` - cadastrar aluno
- `GET /api/alunos/{id}` - buscar aluno por ID
- `GET /api/alunos/matricula/{matricula}` - buscar aluno por matrícula
- `PUT /api/alunos/{id}` - atualizar aluno
- `DELETE /api/alunos/{id}` - remover aluno
- `POST /api/desempenhos` - registrar desempenho de aluno
- `GET /api/disciplinas/autocompletar?q={prefixo}` - autocompletar disciplinas
- `GET /api/ranking?top={k}` - obter ranking de alunos
- `GET /api/admin/health` - status de saúde
- `GET /api/admin/cache/stats` - estatísticas de cache
- `DELETE /api/admin/cache` - limpar cache
- `GET /api/admin/rate-limiter/stats?cliente={id}` - estatísticas do rate limiter

## Mensageria

- Exchange: `edumetrics.relatorios`
- Fila principal: `relatorios.processamento`
- DLQ: `relatorios.dlq`
- Eventos são enviados quando um novo desempenho é registrado.

## Configuração de teste local

O módulo de teste define `src/test/resources/application.properties` para:

- cache simples em vez de Redis
- desativar inicialização automática dos listeners RabbitMQ
- evitar falhas de fila ausente durante testes unitários e de integração leves

## Tabela de resultados JMH

O README deve conter a tabela de resultados reais obtidos com o benchmark. Abaixo está o template exigido pelo tópico 7.3. Preencha os valores após rodar o benchmark.

| Benchmark | Score | Erro ± | Unidade | Análise |
|---|---|---|---|---|
| `buscaLRUCache` | 0,077 | 0,015 | µs/op | Comparar overhead do LRU em relação ao acesso direto de mapa; ambos são O(1), mas LRU tem lista duplamente encadeada. |
| `buscaHashMapDireto` | 0,031 | 0,016 | µs/op | Verificar custo base do acesso direto em HashMap; deve ser menor ou igual ao LRUCache. |
| `autocompletarTrie` | 0,353 | 0,021 | µs/op | Espera-se maior eficiência para prefixo com grande vocabulário; complexidade O(p + r), onde p é tamanho do prefixo. |
| `autocompletarLinear` | 0,202 | 0,015 | µs/op | Comparar com varredura linear; complexidade O(n) no número de disciplinas, que cresce muito mais rápido que Trie. |
| `rateLimiterCheck` | 0,025 | 0,004 | µs/op | Avaliar custo amortizado do rate limiter; se > 1 µs, sincronização pode ser gargalo. |

### O que analisar nos resultados JMH

1. LRU Cache vs HashMap: esperamos tempos próximos. O LRU deve ser mais lento que HashMap direto devido à manutenção da ordem de acesso.
2. Trie vs varredura linear: com poucas disciplinas a diferença pode ser pequena. Com 10.000 disciplinas, a Trie escala melhor porque evita a varredura completa do conjunto.
3. Rate Limiter: o custo deve ser O(1) amortizado. Se o tempo ficar elevado, o uso de `synchronized` no deque pode ser o motivo.
4. Variância: erro ± > 10% indica instabilidade de benchmark. Pode ser causado por GC, JIT não estabilizado ou carga do sistema.
