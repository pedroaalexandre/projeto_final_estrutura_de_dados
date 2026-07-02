package br.edu.iftm.ed.edumetrics.benchmark;

import br.edu.iftm.ed.edumetrics.domain.dto.AlunoDTO;
import br.edu.iftm.ed.edumetrics.estruturas.LRUCache;
import br.edu.iftm.ed.edumetrics.estruturas.RateLimiter;
import br.edu.iftm.ed.edumetrics.estruturas.Trie;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

import java.lang.reflect.Field;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Benchmark)
@Fork(value = 1)
public class EduMetricsBenchmark {

    private static final int N = 10_000;
    private static final int DISCIPLINAS = 6;
    private static final int MAX_RESULTADOS = 10;
    private static final int RATE_CLIENTES = 10;

    private List<String> matriculas;
    private LRUCache<String, AlunoDTO> lruCache;
    private Map<String, AlunoDTO> hashMapIndice;
    private Trie trie;
    private List<String> disciplinas;
    private RateLimiter rateLimiter;
    private List<String> clientes;

    @Setup(Level.Trial)
    public void setup() throws Exception {
        matriculas = new ArrayList<>(N);
        lruCache = new LRUCache<>(N);
        hashMapIndice = new HashMap<>(N);
        for (int i = 0; i < N; i++) {
            String matricula = String.format("MAT%05d", i);
            AlunoDTO aluno = new AlunoDTO((long) i, matricula, "Aluno " + i, "aluno" + i + "@iftm.edu.br", "Engenharia", 1);
            matriculas.add(matricula);
            lruCache.put(matricula, aluno);
            hashMapIndice.put(matricula, aluno);
        }

        trie = new Trie();
        disciplinas = List.of(
                "Algoritmos e Estruturas de Dados",
                "Programação Orientada a Objetos",
                "Banco de Dados",
                "Sistemas Operacionais",
                "Engenharia de Software",
                "Redes de Computadores"
        );
        for (String disciplina : disciplinas) {
            trie.inserir(disciplina);
        }

        rateLimiter = new RateLimiter();
        setPrivateField(rateLimiter, "maxRequisicoes", 100);
        setPrivateField(rateLimiter, "janelaMilissegundos", 60_000L);
        clientes = new ArrayList<>(RATE_CLIENTES);
        for (int i = 0; i < RATE_CLIENTES; i++) {
            clientes.add("cliente-" + i);
        }
    }

    @Benchmark
    public AlunoDTO buscaLRUCache(Blackhole bh) {
        String mat = matriculas.get(ThreadLocalRandom.current().nextInt(N));
        AlunoDTO dto = lruCache.get(mat);
        bh.consume(dto);
        return dto;
    }

    @Benchmark
    public AlunoDTO buscaHashMapDireto(Blackhole bh) {
        String mat = matriculas.get(ThreadLocalRandom.current().nextInt(N));
        AlunoDTO dto = hashMapIndice.get(mat);
        bh.consume(dto);
        return dto;
    }

    @Benchmark
    public List<String> autocompletarTrie(Blackhole bh) {
        String pref = disciplinas.get(ThreadLocalRandom.current().nextInt(disciplinas.size())).substring(0, 5);
        List<String> r = trie.autocompletar(pref, MAX_RESULTADOS);
        bh.consume(r);
        return r;
    }

    @Benchmark
    public List<String> autocompletarLinear(Blackhole bh) {
        String pref = disciplinas.get(ThreadLocalRandom.current().nextInt(disciplinas.size())).substring(0, 5).toLowerCase();
        List<String> r = new ArrayList<>();
        for (String nome : disciplinas) {
            if (nome.toLowerCase().startsWith(pref)) {
                r.add(nome);
                if (r.size() >= MAX_RESULTADOS) {
                    break;
                }
            }
        }
        bh.consume(r);
        return Collections.unmodifiableList(r);
    }

    @Benchmark
    public boolean rateLimiterCheck(Blackhole bh) {
        String clienteId = clientes.get(ThreadLocalRandom.current().nextInt(clientes.size()));
        boolean ok = rateLimiter.permitir(clienteId);
        bh.consume(ok);
        return ok;
    }

    private void setPrivateField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }
}
