package currentTest;

import java.util.ArrayList;

public class Quota {
    ArrayList<Sample> arrayList1 = new ArrayList(2);

    public  void check() {
        try {
            if (arrayList1.isEmpty()) {
                arrayList1.add(new Sample("1", System.currentTimeMillis()));
            }
            for (int i = 0; i < arrayList1.size(); i++) {
                System.out.println(arrayList1.get(i).lastMs + "_" + i + Thread.currentThread().getName() +"sss"+ arrayList1.size());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class Sample {
        String name;
        long lastMs;

        public Sample(String name, long lastMs) {
            this.name = name;
            this.lastMs = lastMs;
        }
    }
}
