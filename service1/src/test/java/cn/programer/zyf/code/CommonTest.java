package cn.programer.zyf.code;

import org.assertj.core.util.Lists;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;

/**
 * @Author wb-zyf471922
 * @Date 2019/11/29 20:28
 **/
public class CommonTest {

    @Test
    public void testSort() {
        List<Long> list = Lists.newArrayList(1L, 2L, 4L, 3L, 1L);

        list.sort(Comparator.comparing(Long::longValue));
        System.out.println(list);
    }

    @Test
    public void testGroupBy() {
        List<Long> list = Lists.newArrayList(1L, 2L, 4L, 3L, 1L);

        Map<Long, List<Long>> map = list.stream()
            .collect(groupingBy(Long::longValue));

        System.out.println(map);
    }

    @Test
    public void testFilter() {
        List<Long> list = Lists.newArrayList(1L, 2L, 4L, 3L, 1L);

        list = list.stream()
            .filter(a -> a == 1)
            .collect(Collectors.toList());

        System.out.println(list);
    }

    @Test
    public void testForEach() {
        List<Long> list = Lists.newArrayList(1L, 2L, 4L, 3L, 1L);

        list.forEach((a) -> {System.out.println(a);});
    }

    @Test
    public void testCompareAndCompare() {
        List<Long> list = Lists.newArrayList(1L, 2L, 4L, 3L, 1L);

        list.sort(Comparator.comparing(Long::longValue).reversed().thenComparing(Long::longValue));
        System.out.println(list);
    }

    @Test
    public void testFlatMap() {
        List<String> list = Lists.newArrayList("hello", "world", "zyf");
        List<String> characters = list.stream().map(a -> a.split(""))
            .flatMap(Arrays::stream)
            .distinct()
            .collect(Collectors.toList());
        System.out.println(characters);
    }

    @Test
    public void testFlatMap2() {
        List<Integer> list1 = Lists.newArrayList(1,2,3);
        List<Integer> list2 = Lists.newArrayList(5,6);
        List<Integer[]> result = list1.stream()
            .flatMap(a -> list2.stream().map(b -> new Integer[]{a, b}))
            .collect(Collectors.toList());

        result.forEach(a -> {
            Arrays.stream(a).forEach(System.out::print);
            System.out.println();
        });
    }

    @Test
    public void testFlatMap3() {
        List<Integer> list1 = Lists.newArrayList(1,2,3);
        List<Integer> list2 = Lists.newArrayList(5,6);
        List<Integer[]> result = new ArrayList<>();
        list2.forEach(a -> {
            list1.forEach(b -> {
                result.add(new Integer[]{b, a});
            });
        });

        result.forEach(a -> {
            Arrays.stream(a).forEach(System.out::print);
            System.out.println();
        });
    }

    private void executor1(List<Integer> list1, List<Integer> list2) {
        List<Integer[]> result = list1.stream()
            .flatMap(a -> list2.stream().map(b -> new Integer[]{a, b}))
            .collect(Collectors.toList());

        //result.forEach(a -> {
        //    Arrays.stream(a).forEach(System.out::print);
        //    System.out.println();
        //});
    }

    private void executor2(List<Integer> list1, List<Integer> list2) {
        List<Integer[]> result = new ArrayList<>();
        list2.forEach(a -> {
            list1.forEach(b -> {
                result.add(new Integer[]{b, a});
            });
        });

        //result.forEach(a -> {
        //    Arrays.stream(a).forEach(System.out::print);
        //    System.out.println();
        //});
    }

    @Test
    public void testHAHA() {
        List<Integer> list1 = new ArrayList<>();
        List<Integer> list2 = new ArrayList<>();
        for (int i = 0; i <= 30000; i++) {
            list1.add(i);
        }

        for (int i = 0; i <= 10; i++) {
            list2.add(i);
        }

        Long t1 = System.currentTimeMillis();
        for (int i = 0; i <= 10; i++) {
            executor1(list1, list2);
        }
        Long t2 = System.currentTimeMillis();
        for (int i = 0; i <= 10; i++) {
            executor2(list1, list2);
        }
        Long t3 = System.currentTimeMillis();
        System.out.println("flatMap执行" + (t2 - t1) + "ms\n"
            + "foreach执行" + (t3 - t2) + "ms");
    }

    @Test
    public void testFind() {
        List<Integer> list = Lists.newArrayList(1, 2, 3, 4, 5);
        list.stream().filter(a -> a == 3)
            .findFirst()
            .ifPresent(System.out::println);
    }

    @Test
    public void testIntStream() {
        int[] array = new int[]{1,2,3};

        Arrays.stream(array).forEach(System.out::println);
    }

    @Test
    public void testRange() {
        List<Integer> list = IntStream.range(1, 100)
            .filter(a -> a % 4 == 0)
            .boxed()
            .collect(Collectors.toList());
        System.out.println(list);
    }

    @Test
    public void testGenerateGouguNumber() {
        List<int[]> list = new ArrayList<>();
        IntStream.rangeClosed(1, 100).forEach(a -> {
            IntStream.rangeClosed(a + 1, 100)
                .filter(b -> Math.sqrt(b * b - a * a) % 1 == 0)
                .filter(b -> Math.sqrt(b * b - a * a) >= a)
                .forEach(b -> {
                    list.add(new int[]{a, b, (int)Math.sqrt(b * b - a * a)});
                });
        });

        list.forEach(a -> {
            Arrays.stream(a).forEach(b -> {System.out.print(b + " ");});
            System.out.println();
        });
    }

    @Test
    public void testFeibonaqie() {
        List<int[]> list = new ArrayList<>();

        list = Stream.iterate(new int[]{1, 1}, a -> new int[]{a[1], a[0] + a[1]})
            .limit(10)
            .collect(Collectors.toList());

        list.forEach(a -> {
            Arrays.stream(a).forEach(b -> {System.out.print(b + " ");});
            System.out.println();
        });
    }

    @Test
    public void testCollector() {
        List<Integer> list = Lists.newArrayList(1,2,3,4);
        System.out.println(list.stream().min(Comparator.comparing(Integer::intValue)));
    }

    @Test
    public void testOptional() {
        Optional<Integer> optional = Optional.of(1);
        optional.map(Integer::intValue);
    }

    @Test
    public void testBigDecimal() {
        BigDecimal num = new BigDecimal(".03").setScale(4);
        System.out.println(num.toPlainString());
    }

    @Test
    public void testAnyMatch() {
        List<String> list = com.google.common.collect.Lists.newArrayList("1","2","3");
        boolean b = list.stream().anyMatch(a -> "2".equals(a));
        System.out.println(b);
    }

}
