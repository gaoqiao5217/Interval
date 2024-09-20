import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class Interval {

    public static void main(String[] args) throws Exception {
        String[] texts = new String[25];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("aab", 30_000);
        }

        long startTs = System.currentTimeMillis();

        ExecutorService executor = Executors.newFixedThreadPool(texts.length);
        Future<Integer>[] futures = new Future[texts.length];

        for (int i = 0; i < texts.length; i++) {
            final String text = texts[i];
            futures[i] = executor.submit(() -> {
                int maxSize = 0;
                for (int j = 0; j < text.length(); j++) {
                    for (int k = j + 1; k < text.length(); k++) {
                        boolean bFound = false;
                        for (int l = j; l < k; l++) {
                            if (text.charAt(l) == 'b') {
                                bFound = true;
                                break;
                            }
                        }
                        if (!bFound && maxSize < k - j) {
                            maxSize = k - j;
                        }
                    }
                }
                System.out.println(text.substring(0, 100) + " -> " + maxSize);
                return maxSize;
            });
        }

        int totalMaxSize = 0;
        for (Future<Integer> future : futures) {
            totalMaxSize += future.get();
        }

        executor.shutdown();

        long finishTs = System.currentTimeMillis();
        System.out.println("Общая максимальная длина подстрок: " + totalMaxSize);
        System.out.println("Время выполнения: " + (finishTs - startTs) + " ms");
    }

    private static String generateText(String pattern, int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(pattern.charAt((int)(Math.random() * pattern.length())));
        }
        return sb.toString();
    }
}