package javaapplication4;

import static java.lang.Thread.sleep;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Multithread {

    public static void main(String[] args) {

        Giris g = new Giris(ThreadRenkler.Blue);
        Thread giriss = new Thread(g);
        giriss.setName("Giriş Thread");
        giriss.start();

        try {
            giriss.join(520);
        } catch (InterruptedException ex) {
            Logger.getLogger(Multithread.class.getName()).log(Level.SEVERE, null, ex);
        }

        Cikis c = new Cikis(ThreadRenkler.Red);
        Thread cikiss = new Thread(c);
        cikiss.setName("Çıkış Thread");
        cikiss.start();

        Asansor1 a1 = new Asansor1();
        Thread asansor = new Thread(a1);
        asansor.setDaemon(true);
        asansor.setName("Asansör 1 Thread");

        try {
            giriss.join(520);
        } catch (InterruptedException ex) {
            Logger.getLogger(Multithread.class.getName()).log(Level.SEVERE, null, ex);
        }

        asansor.start();

    }
}

class Giris implements Runnable {

    // 500 ms arayla 1-10 arasinda rastgele sayida müsteri avmye giris yapar. 1-4 arasinda rastgele kata giderler.
    BlockingQueue<Integer> kuyruk = new ArrayBlockingQueue<>(500);
    BlockingQueue<Integer> hedefkat1 = new ArrayBlockingQueue<>(500);
    BlockingQueue<Integer> hedefkat2 = new ArrayBlockingQueue<>(500);
    BlockingQueue<Integer> hedefkat3 = new ArrayBlockingQueue<>(500);
    BlockingQueue<Integer> hedefkat4 = new ArrayBlockingQueue<>(500);
    private final String renk;

    public Giris(String renk) {
        this.renk = renk;
    }

    @Override
    @SuppressWarnings("SleepWhileInLoop")
    public void run() {

        Asansor1 a1 = new Asansor1();
        int sayac1 = 0, sayac2 = 0, sayac3 = 0, sayac4 = 0;

        System.out.println(renk + "Program başlatıldı: " + Thread.currentThread().getName());

        Integer[] musteriVeGidecegiKat = new Integer[10];
        int girisYapanMusteriSayisi;

        int kontrolSayaci = 0;
        for (int j = 0; j < 5; j++) {

            Random r = new Random();
            girisYapanMusteriSayisi = r.nextInt(10) + 1;   // 10 dahil degil
            //System.out.println(renk + "giriş yapan kişi sayısı: " + girisYapanMusteriSayisi);
            kontrolSayaci += girisYapanMusteriSayisi;
            Kontrol kontrol = new Kontrol(kontrolSayaci);

            //System.out.println(renk + "Zemin Kat: " + "Kuyruğu: " + kontrol.musteriSayisi);

            int i = 0;
            while (i < girisYapanMusteriSayisi) {

                Random rn = new Random();
                musteriVeGidecegiKat[i] = rn.nextInt(4) + 1; // 5 dahil degil

                if (null != musteriVeGidecegiKat[i]) {
                    switch (musteriVeGidecegiKat[i]) {
                        case 1 -> {
                            try {
                                kuyruk.offer(1);
                                hedefkat1.put(1);
                                sayac1++;
                            } catch (InterruptedException ex) {
                                Logger.getLogger(Giris.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            //System.out.println(renk + "kuyruk1" + hedefkat1.size());
                            sayac1++;
                        }
                        case 2 -> {
                            try {
                                kuyruk.offer(2);
                                hedefkat2.put(2);
                                sayac2++;
                            } catch (InterruptedException ex) {
                                Logger.getLogger(Giris.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }

                        case 3 -> {
                            try {
                                kuyruk.offer(3);
                                hedefkat3.put(3);
                                sayac3++;
                            } catch (InterruptedException ex) {
                                Logger.getLogger(Giris.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }

                        case 4 -> {
                            try {
                                kuyruk.offer(4);
                                hedefkat4.put(4);
                                sayac4++;
                            } catch (InterruptedException ex) {
                                Logger.getLogger(Giris.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }

                        default -> {

                        }
                    }
                }

                //System.out.println(renk + "gidilecek kat: " + musteriVeGidecegiKat[i]);
                i++;
            }
            a1.AsansorYukari(hedefkat1, hedefkat2, hedefkat3, hedefkat4);
            synchronized (this) {

                a1.YukariGit();
            }
            
            try {

                long start = System.currentTimeMillis();
                sleep(500);

                //System.out.println(renk + "Uyunan zaman = " + (System.currentTimeMillis() - start));
            } catch (InterruptedException ex) {
                Logger.getLogger(Giris.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }
}

class Cikis implements Runnable {

    Asansor1 a1 = new Asansor1();
    BlockingQueue<Integer> katKuyruk1 = new ArrayBlockingQueue<>(500);
    BlockingQueue<Integer> katKuyruk2 = new ArrayBlockingQueue<>(500);
    BlockingQueue<Integer> katKuyruk3 = new ArrayBlockingQueue<>(500);
    BlockingQueue<Integer> katKuyruk4 = new ArrayBlockingQueue<>(500);

    // 1000 ms arayla 1-5 arasinda rastgele sayida müsteri avmden cikis yapar(Zemin kata iner).
    private final String renk;

    public Cikis(String renk) {
        this.renk = renk;
    }

    @Override
    @SuppressWarnings("SleepWhileInLoop")
    public void run() {

        int sayac1 = 0, sayac2 = 0, sayac3 = 0, sayac4 = 0;

        //Asansor1 a1 = new Asansor1();
        System.out.println(renk + "Program başlatıldı: " + Thread.currentThread().getName());
        Integer[] musteriVeBulunduguKat = new Integer[10];
        int cikisYapanMusteriSayisi;

        for (int j = 0; j < 5; j++) {

            Random ran = new Random();
            cikisYapanMusteriSayisi = ran.nextInt(5) + 1;   // 5 dahil 
            //System.out.println(renk + "çıkış yapmak isteyen kişi sayısı: " + cikisYapanMusteriSayisi);
            int i = 0;
            while (i < cikisYapanMusteriSayisi) {

                Random rn = new Random();
                musteriVeBulunduguKat[i] = rn.nextInt(4) + 1; // 4 dahil

                if (musteriVeBulunduguKat[i] == 1) {
                    sayac1++;
                }
                if (musteriVeBulunduguKat[i] == 2) {
                    sayac2++;
                }
                if (musteriVeBulunduguKat[i] == 3) {
                    sayac3++;
                }
                if (musteriVeBulunduguKat[i] == 4) {
                    sayac4++;
                }
                //System.out.println(renk + "Müşterilerin bulunduğu kat: " + musteriVeBulunduguKat[i]);

                i++;
            }
            for (int k = 0; k < sayac1 - katKuyruk1.size(); k++) {
                try {
                    katKuyruk1.put(0);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Cikis.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            for (int k = 0; k < sayac2 - katKuyruk2.size(); k++) {
                try {
                    katKuyruk2.put(0);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Cikis.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            for (int k = 0; k < sayac3 - katKuyruk3.size(); k++) {
                try {
                    katKuyruk3.put(0);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Cikis.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            for (int k = 0; k < sayac4 - katKuyruk4.size(); k++) {

                try {
                    katKuyruk4.put(0);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Cikis.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            System.out.println(renk + "1. Kat: " + "  Toplam: 20 " + " Kuyruk: " + katKuyruk1.size());
            System.out.println(renk + "2. Kat: " + "  Toplam: 20 " + " Kuyruk: " + katKuyruk2.size());
            System.out.println(renk + "3. Kat: " + "  Toplam: 20 " + " Kuyruk: " + katKuyruk3.size());
            System.out.println(renk + "4. Kat: " + "  Toplam: 20 " + " Kuyruk: " + katKuyruk4.size());

            a1.AsansorAsagi(katKuyruk1, katKuyruk2, katKuyruk3, katKuyruk4);
            synchronized (this){
                a1.AsagiGit();
            }
            
            try {

                long start = System.currentTimeMillis();
                sleep(1000);

                //System.out.println(renk + "Uyunan zaman = " + (System.currentTimeMillis() - start));
            } catch (InterruptedException ex) {
                Logger.getLogger(Cikis.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
class TumKatlar {

    int kat1 = 20, kat2 = 20, kat3 = 20, kat4 = 20;

    public TumKatlar() {
    }

    public int getKat1() {
        return kat1;
    }

    public void setKat1(int kat1) {
        this.kat1 = kat1;

    }

    public int getKat2() {
        return kat2;
    }

    public void setKat2(int kat2) {
        this.kat2 = kat2;

    }

    public int getKat3() {
        return kat3;
    }

    public void setKat3(int kat3) {
        this.kat3 = kat3;

    }

    public int getKat4() {
        return kat4;
    }

    public void setKat4(int kat4) {
        this.kat4 = kat4;

    }

}

class Kontrol implements Runnable {

    // Katlardaki kuyruklari kontrol eder. Kuyruktaki kisiler 20'yi asarsa ikinci asansoru aktif eder. 
    // Toplam bekleyen sayisi asansor kapasitesinin altina indiginde asansor pasiflesir(İlk asansor haric).
    int musteriSayisi;
    int kuyruktakiSayi;

    public Kontrol(int musteriSayisi) {
        this.musteriSayisi = musteriSayisi;
    }

    @Override
    public String toString() {
        return "" + musteriSayisi;
    }

    @Override
    public void run() {

        if (musteriSayisi > 20) {
            Asansor2 a2 = new Asansor2();
            Thread asansor2 = new Thread(a2);
            asansor2.setDaemon(true);
            asansor2.setName("Asansör 2 Thread");
            asansor2.start();

            if (musteriSayisi > 40) {
                Asansor3 a3 = new Asansor3();
                Thread asansor3 = new Thread(a3);
                asansor3.setDaemon(true);
                asansor3.setName("Asansör 3 Thread");
                asansor3.start();
                if (musteriSayisi > 60) {
                    Asansor4 a4 = new Asansor4();
                    Thread asansor4 = new Thread(a4);
                    asansor4.setDaemon(true);
                    asansor4.setName("Asansör 4 Thread");
                    asansor4.start();
                    if (musteriSayisi > 80) {
                        Asansor5 a5 = new Asansor5();
                        Thread asansor5 = new Thread(a5);
                        asansor5.setDaemon(true);
                        asansor5.setName("Asansör 5 Thread");
                        asansor5.start();
                    }
                }
            }
        }

    }
}

class Asansor1 implements Runnable {

    // Katlardaki kuyruklari kontrol eder. En fazla 10 kisi alir. Müsterileri talep ettigi kata tasir. 
    boolean aktifMi = true;
    int hedefKat;
    String yon;
    final int kapasite = 10;

    int toplamKuyruktakiler;
    int sayac1 = 0, sayac2 = 0, sayac3 = 0, sayac4 = 0;
    int asansor1_inenler = 0;
    int kat;

    TumKatlar katlar = new TumKatlar();

    public BlockingQueue<Integer> kuyruk = new ArrayBlockingQueue<>(500);
    public BlockingQueue<Integer> katKuyruk1 = new ArrayBlockingQueue<>(500);
    public BlockingQueue<Integer> katKuyruk2 = new ArrayBlockingQueue<>(500);
    public BlockingQueue<Integer> katKuyruk3 = new ArrayBlockingQueue<>(500);
    public BlockingQueue<Integer> katKuyruk4 = new ArrayBlockingQueue<>(500);

    public BlockingQueue<Integer> hedefkat1 = new ArrayBlockingQueue<>(500);
    public BlockingQueue<Integer> hedefkat2 = new ArrayBlockingQueue<>(500);
    public BlockingQueue<Integer> hedefkat3 = new ArrayBlockingQueue<>(500);
    public BlockingQueue<Integer> hedefkat4 = new ArrayBlockingQueue<>(500);

    void AsansorYukari(BlockingQueue<Integer> hedefkat1, BlockingQueue<Integer> hedefkat2, BlockingQueue<Integer> hedefkat3, BlockingQueue<Integer> hedefkat4) {
        this.hedefkat1 = hedefkat1;
        this.hedefkat2 = hedefkat2;
        this.hedefkat3 = hedefkat3;
        this.hedefkat4 = hedefkat4;
    }

    void AsansorAsagi(BlockingQueue<Integer> katKuyruk1, BlockingQueue<Integer> katKuyruk2, BlockingQueue<Integer> katKuyruk3, BlockingQueue<Integer> katKuyruk4) {
        this.katKuyruk1 = katKuyruk1;
        this.katKuyruk2 = katKuyruk2;
        this.katKuyruk3 = katKuyruk3;
        this.katKuyruk4 = katKuyruk4;

    }

    public synchronized void YukariGit() {
        System.out.println("\n");
        System.out.println("ASANSOR 1");
        System.out.println("Yukarı Gidiyor...");
        String mode = "çalışıyor";
        yon = "Yukarı";
        System.out.println("Aktiflik: " + aktifMi);
        int toplamKuyruk;
        toplamKuyruk = hedefkat1.size() + hedefkat2.size() + hedefkat3.size() + hedefkat4.size();

        System.out.println("Mod: " + mode);
        System.out.println("Bulunulan Kat: 0");
        System.out.println("Yön: " + yon);
        System.out.println("Kapasite: " + kapasite);
        if (toplamKuyruk < 10) {
            System.out.println("Asansördeki Kişi Sayısı: " + toplamKuyruk); /// Sıkıntııı
        } else {
            toplamKuyruk = 10;
            System.out.println("Asansördeki Kişi Sayısı: " + toplamKuyruk);
        }

        if (!hedefkat1.isEmpty()) {
            try {
                sleep(200);
            } catch (InterruptedException ex) {
                Logger.getLogger(Asansor1.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("Mod: " + mode);
            System.out.println("Bulunulan Kat: 1");
            System.out.println("Yön: " + yon);
            System.out.println("Kapasite: " + kapasite);
        }

        while (!hedefkat1.isEmpty()) {

            toplamKuyruk = hedefkat1.size() + hedefkat2.size() + hedefkat3.size() + hedefkat4.size();
            System.out.println("Asansördeki Kişi Sayısı: " + toplamKuyruk); /// Sıkıntııı
            System.out.println("Anlık Asansörde Bulunan Kişiler ve Gidecekleri Katlar: (" + hedefkat1.size() + ",1), (" + hedefkat2.size() + ",2), (" + hedefkat3.size() + ",3), (" + hedefkat4.size() + ",4)"); ///Sıkıntıııı
            hedefkat1.poll();

        }
        if (!hedefkat2.isEmpty()) {
            try {
                sleep(200);
            } catch (InterruptedException ex) {
                Logger.getLogger(Asansor1.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("Mod: " + mode);
            System.out.println("Bulunulan Kat: 2");
            System.out.println("Yön: " + yon);
            System.out.println("Kapasite: " + kapasite);
        }

        while (!hedefkat2.isEmpty()) {
            toplamKuyruk = hedefkat1.size() + hedefkat2.size() + hedefkat3.size() + hedefkat4.size();
            System.out.println("Asansördeki Kişi Sayısı: " + toplamKuyruk); /// Sıkıntııı

            System.out.println("Anlık Asansörde Bulunan Kişiler ve Gidecekleri Katlar: (" + hedefkat2.size() + ",2), (" + hedefkat3.size() + ",3), (" + hedefkat4.size() + ",4)"); ///Sıkıntıııı
            hedefkat2.poll();

        }
        if (!hedefkat3.isEmpty()) {
            try {
                sleep(200);
            } catch (InterruptedException ex) {
                Logger.getLogger(Asansor1.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("Mod: " + mode);
            System.out.println("Bulunulan Kat: 3");
            System.out.println("Yön: " + yon);
            System.out.println("Kapasite: " + kapasite);
        }

        while (!hedefkat3.isEmpty()) {
            toplamKuyruk = hedefkat1.size() + hedefkat2.size() + hedefkat3.size() + hedefkat4.size();
            System.out.println("Asansördeki Kişi Sayısı: " + toplamKuyruk); /// Sıkıntııı
            System.out.println("Anlık Asansörde Bulunan Kişiler ve Gidecekleri Katlar: (" + hedefkat3.size() + ",3), (" + hedefkat4.size() + ",4)"); ///Sıkıntıııı
            System.out.println("kuyruğun uzunluğu: " + hedefkat3.size());
            hedefkat3.poll();
        }
        if (!hedefkat4.isEmpty()) {
            try {
                sleep(200);
            } catch (InterruptedException ex) {
                Logger.getLogger(Asansor1.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("Mod: " + mode);
            System.out.println("Bulunulan Kat: 4");
            System.out.println("Yön: " + yon);
            System.out.println("Kapasite: " + kapasite);
        }

        while (!hedefkat4.isEmpty()) {
            toplamKuyruk = hedefkat1.size() + hedefkat2.size() + hedefkat3.size() + hedefkat4.size();
            System.out.println("Asansördeki Kişi Sayısı: " + toplamKuyruk); /// Sıkıntııı
            System.out.println("Anlık Asansörde Bulunan Kişiler ve Gidecekleri Katlar: (" + hedefkat4.size() + ",4)"); ///Sıkıntıııı
            hedefkat4.poll();
        }
        System.out.println("Asansör En Üste Çıktı...");
        System.out.println("\n\n");
    }

    public synchronized void AsagiGit() {
        System.out.println("\n");
        System.out.println("ASANSOR 1");
        System.out.println("Asansör Aşağı İniyor...");
        String mode = "çalışıyor";
        yon = "Aşağı";
        System.out.println("Aktiflik: " + aktifMi);
        int toplamKuyruk;
        toplamKuyruk = katKuyruk4.size();

        System.out.println("Mod: " + mode);
        System.out.println("Bulunulan Kat: 4");
        System.out.println("Yön: " + yon);
        System.out.println("Kapasite: " + kapasite);
        if (toplamKuyruk < 10) {
            System.out.println("Asansördeki Kişi Sayısı: " + toplamKuyruk); /// Sıkıntııı
        } else {
            toplamKuyruk = 10;
            System.out.println("Asansördeki Kişi Sayısı: " + toplamKuyruk);
        }
        toplamKuyruk = katKuyruk4.size();
        System.out.println("Asansördeki Kişi Sayısı: " + toplamKuyruk); /// Sıkıntııı
        System.out.println("Anlık Asansörde Bulunan Kişiler ve Gidecekleri Katlar: (" + toplamKuyruk + ",0)"); ///Sıkıntıııı

        while (!katKuyruk4.isEmpty()) {
            katKuyruk4.poll(); // sil
        }
        try {
            sleep(200);
        } catch (InterruptedException ex) {
            Logger.getLogger(Asansor1.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (!katKuyruk3.isEmpty()) {

            System.out.println("Mod: " + mode);
            System.out.println("Bulunulan Kat: 3");
            System.out.println("Yön: " + yon);
            System.out.println("Kapasite: " + kapasite);
        }
        toplamKuyruk += katKuyruk3.size();
        System.out.println("Asansördeki Kişi Sayısı: " + toplamKuyruk); /// Sıkıntııı
        System.out.println("Anlık Asansörde Bulunan Kişiler ve Gidecekleri Katlar: (" + toplamKuyruk + ",0)"); ///Sıkıntıııı
        while (!katKuyruk3.isEmpty()) {
            katKuyruk3.poll();
        }
        try {
            sleep(200);
        } catch (InterruptedException ex) {
            Logger.getLogger(Asansor1.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (!katKuyruk2.isEmpty()) {
            System.out.println("Mod: " + mode);
            System.out.println("Bulunulan Kat: 2");
            System.out.println("Yön: " + yon);
            System.out.println("Kapasite: " + kapasite);
        }
        toplamKuyruk += katKuyruk2.size();
        while (!katKuyruk2.isEmpty()) {
            System.out.println("Asansördeki Kişi Sayısı: " + toplamKuyruk); /// Sıkıntııı
            System.out.println("Anlık Asansörde Bulunan Kişiler ve Gidecekleri Katlar: (" + toplamKuyruk + ",0)"); ///Sıkıntıııı
            katKuyruk2.poll();
        }
        try {
            sleep(200);
        } catch (InterruptedException ex) {
            Logger.getLogger(Asansor1.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (!katKuyruk1.isEmpty()) {

            System.out.println("Mod: " + mode);
            System.out.println("Bulunulan Kat: 1");
            System.out.println("Yön: " + yon);
            System.out.println("Kapasite: " + kapasite);
        }
        toplamKuyruk += katKuyruk1.size();

        System.out.println("Asansördeki Kişi Sayısı: " + toplamKuyruk); /// Sıkıntııı
        System.out.println("Anlık Asansörde Bulunan Kişiler ve Gidecekleri Katlar: (" + toplamKuyruk + ",0)"); ///Sıkıntıııı
        while (!katKuyruk1.isEmpty()) {
            katKuyruk1.poll();
        }
        System.out.println("Mod: " + mode);
        System.out.println("Bulunulan Kat: 0");
        System.out.println("Yön: " + yon);
        System.out.println("Kapasite: " + kapasite);
         System.out.println("Asansördeki Kişi Sayısı: 0"); /// Sıkıntııı
        System.out.println("Anlık Asansörde Bulunan Kişiler ve Gidecekleri Katlar: (0,0)"); ///Sıkıntıııı
        
        System.out.println("Asansör En Alta İndi...");
        System.out.println("\n\n");
    }

    @Override
    @SuppressWarnings("SleepWhileInLoop")
    public void run() {

        int k1 = 0;

        while (k1 < 10) {

            try {

                long start = System.currentTimeMillis();
                Thread.sleep(200);

                System.out.println("Uyunan zaman = " + (System.currentTimeMillis() - start));
            } catch (InterruptedException ex) {
                Logger.getLogger(Asansor1.class.getName()).log(Level.SEVERE, null, ex);
            }

            k1++;
        }
    }
}

class Asansor2 implements Runnable {

    // Katlardaki kuyruklari kontrol eder. En fazla 10 kisi alir. Müsterileri talep ettigi kata tasir. 
    boolean aktifMi = true;
    int hedefKat;
    String yon;
    final int kapasite = 10;

    int toplamKuyruktakiler;
    int sayac1 = 0, sayac2 = 0, sayac3 = 0, sayac4 = 0;
    int asansor1_inenler = 0;
    int kat;

    TumKatlar katlar = new TumKatlar();

    public BlockingQueue<Integer> kuyruk = new ArrayBlockingQueue<>(500);
    public BlockingQueue<Integer> katKuyruk1 = new ArrayBlockingQueue<>(500);
    public BlockingQueue<Integer> katKuyruk2 = new ArrayBlockingQueue<>(500);
    public BlockingQueue<Integer> katKuyruk3 = new ArrayBlockingQueue<>(500);
    public BlockingQueue<Integer> katKuyruk4 = new ArrayBlockingQueue<>(500);

    public BlockingQueue<Integer> hedefkat1 = new ArrayBlockingQueue<>(500);
    public BlockingQueue<Integer> hedefkat2 = new ArrayBlockingQueue<>(500);
    public BlockingQueue<Integer> hedefkat3 = new ArrayBlockingQueue<>(500);
    public BlockingQueue<Integer> hedefkat4 = new ArrayBlockingQueue<>(500);

    void AsansorYukari(BlockingQueue<Integer> hedefkat1, BlockingQueue<Integer> hedefkat2, BlockingQueue<Integer> hedefkat3, BlockingQueue<Integer> hedefkat4) {
        this.hedefkat1 = hedefkat1;
        this.hedefkat2 = hedefkat2;
        this.hedefkat3 = hedefkat3;
        this.hedefkat4 = hedefkat4;
    }

    void AsansorAsagi(BlockingQueue<Integer> katKuyruk1, BlockingQueue<Integer> katKuyruk2, BlockingQueue<Integer> katKuyruk3, BlockingQueue<Integer> katKuyruk4) {
        this.katKuyruk1 = katKuyruk1;
        this.katKuyruk2 = katKuyruk2;
        this.katKuyruk3 = katKuyruk3;
        this.katKuyruk4 = katKuyruk4;

    }

    public synchronized void YukariGit() {

        String mode = "çalışıyor";
        yon = "Yukarı";
        System.out.println("Aktiflik: " + aktifMi);
        int toplamKuyruk;
        toplamKuyruk = hedefkat1.size() + hedefkat2.size() + hedefkat3.size() + hedefkat4.size();

        System.out.println("Mod: " + mode);
        System.out.println("Bulunulan Kat: 0");
        System.out.println("Yön: " + yon);
        System.out.println("Kapasite: " + kapasite);
        if (toplamKuyruk < 10) {
            System.out.println("Asansördeki Kişi Sayısı: " + toplamKuyruk); /// Sıkıntııı
        } else {
            toplamKuyruk = 10;
            System.out.println("Asansördeki Kişi Sayısı: " + toplamKuyruk);
        }

        if (!hedefkat1.isEmpty()) {
            try {
                sleep(200);
            } catch (InterruptedException ex) {
                Logger.getLogger(Asansor1.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("Mod: " + mode);
            System.out.println("Bulunulan Kat: 1");
            System.out.println("Yön: " + yon);
            System.out.println("Kapasite: " + kapasite);
        }

        while (!hedefkat1.isEmpty()) {

            toplamKuyruk = hedefkat1.size() + hedefkat2.size() + hedefkat3.size() + hedefkat4.size();
            System.out.println("Asansördeki Kişi Sayısı: " + toplamKuyruk); /// Sıkıntııı
            System.out.println("Anlık Asansörde Bulunan Kişiler ve Gidecekleri Katlar: (" + hedefkat1.size() + ",1), (" + hedefkat2.size() + ",2), (" + hedefkat3.size() + ",3), (" + hedefkat4.size() + ",4)"); ///Sıkıntıııı
            hedefkat1.poll();

        }
        if (!hedefkat2.isEmpty()) {
            try {
                sleep(200);
            } catch (InterruptedException ex) {
                Logger.getLogger(Asansor1.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("Mod: " + mode);
            System.out.println("Bulunulan Kat: 2");
            System.out.println("Yön: " + yon);
            System.out.println("Kapasite: " + kapasite);
        }

        while (!hedefkat2.isEmpty()) {
            toplamKuyruk = hedefkat1.size() + hedefkat2.size() + hedefkat3.size() + hedefkat4.size();
            System.out.println("Asansördeki Kişi Sayısı: " + toplamKuyruk); /// Sıkıntııı

            System.out.println("Anlık Asansörde Bulunan Kişiler ve Gidecekleri Katlar: (" + hedefkat2.size() + ",2), (" + hedefkat3.size() + ",3), (" + hedefkat4.size() + ",4)"); ///Sıkıntıııı
            hedefkat2.poll();

        }
        if (!hedefkat3.isEmpty()) {
            try {
                sleep(200);
            } catch (InterruptedException ex) {
                Logger.getLogger(Asansor1.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("Mod: " + mode);
            System.out.println("Bulunulan Kat: 3");
            System.out.println("Yön: " + yon);
            System.out.println("Kapasite: " + kapasite);
        }

        while (!hedefkat3.isEmpty()) {
            toplamKuyruk = hedefkat1.size() + hedefkat2.size() + hedefkat3.size() + hedefkat4.size();
            System.out.println("Asansördeki Kişi Sayısı: " + toplamKuyruk); /// Sıkıntııı
            System.out.println("Anlık Asansörde Bulunan Kişiler ve Gidecekleri Katlar: (" + hedefkat3.size() + ",3), (" + hedefkat4.size() + ",4)"); ///Sıkıntıııı
            System.out.println("kuyruğun uzunluğu: " + hedefkat3.size());
            hedefkat3.poll();
        }
        if (!hedefkat4.isEmpty()) {
            try {
                sleep(200);
            } catch (InterruptedException ex) {
                Logger.getLogger(Asansor1.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("Mod: " + mode);
            System.out.println("Bulunulan Kat: 4");
            System.out.println("Yön: " + yon);
            System.out.println("Kapasite: " + kapasite);
        }

        while (!hedefkat4.isEmpty()) {
            toplamKuyruk = hedefkat1.size() + hedefkat2.size() + hedefkat3.size() + hedefkat4.size();
            System.out.println("Asansördeki Kişi Sayısı: " + toplamKuyruk); /// Sıkıntııı
            System.out.println("Anlık Asansörde Bulunan Kişiler ve Gidecekleri Katlar: (" + hedefkat4.size() + ",4)"); ///Sıkıntıııı
            hedefkat4.poll();
        }
        System.out.println("Asansör En Üste Çıktı...");
    }

    public synchronized void AsagiGit() {
        System.out.println("Asansör Aşağı İniyorrr.....");
        String mode = "çalışıyor";
        yon = "Aşağı";
        System.out.println("Aktiflik: " + aktifMi);
        int toplamKuyruk;
        toplamKuyruk = katKuyruk4.size();

        System.out.println("Mod: " + mode);
        System.out.println("Bulunulan Kat: 4");
        System.out.println("Yön: " + yon);
        System.out.println("Kapasite: " + kapasite);
        if (toplamKuyruk < 10) {
            System.out.println("Asansördeki Kişi Sayısı: " + toplamKuyruk); /// Sıkıntııı
        } else {
            toplamKuyruk = 10;
            System.out.println("Asansördeki Kişi Sayısı: " + toplamKuyruk);
        }
        toplamKuyruk = katKuyruk4.size();
        System.out.println("Asansördeki Kişi Sayısı: " + toplamKuyruk); /// Sıkıntııı
        System.out.println("Anlık Asansörde Bulunan Kişiler ve Gidecekleri Katlar: (" + toplamKuyruk + ",0)"); ///Sıkıntıııı

        while (!katKuyruk4.isEmpty()) {
            katKuyruk4.poll(); // sil
        }
        try {
            sleep(200);
        } catch (InterruptedException ex) {
            Logger.getLogger(Asansor1.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (!katKuyruk3.isEmpty()) {

            System.out.println("Mod: " + mode);
            System.out.println("Bulunulan Kat: 3");
            System.out.println("Yön: " + yon);
            System.out.println("Kapasite: " + kapasite);
        }
        toplamKuyruk += katKuyruk3.size();
        System.out.println("Asansördeki Kişi Sayısı: " + toplamKuyruk); /// Sıkıntııı
        System.out.println("Anlık Asansörde Bulunan Kişiler ve Gidecekleri Katlar: (" + toplamKuyruk + ",0)"); ///Sıkıntıııı
        while (!katKuyruk3.isEmpty()) {
            katKuyruk3.poll();
        }
        try {
            sleep(200);
        } catch (InterruptedException ex) {
            Logger.getLogger(Asansor1.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (!katKuyruk2.isEmpty()) {
            System.out.println("Mod: " + mode);
            System.out.println("Bulunulan Kat: 2");
            System.out.println("Yön: " + yon);
            System.out.println("Kapasite: " + kapasite);
        }
        toplamKuyruk += katKuyruk2.size();
        while (!katKuyruk2.isEmpty()) {
            System.out.println("Asansördeki Kişi Sayısı: " + toplamKuyruk); /// Sıkıntııı
            System.out.println("Anlık Asansörde Bulunan Kişiler ve Gidecekleri Katlar: (" + toplamKuyruk + ",0)"); ///Sıkıntıııı
            katKuyruk2.poll();
        }
        try {
            sleep(200);
        } catch (InterruptedException ex) {
            Logger.getLogger(Asansor1.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (!katKuyruk1.isEmpty()) {

            System.out.println("Mod: " + mode);
            System.out.println("Bulunulan Kat: 1");
            System.out.println("Yön: " + yon);
            System.out.println("Kapasite: " + kapasite);
        }
        toplamKuyruk += katKuyruk1.size();

        System.out.println("Asansördeki Kişi Sayısı: " + toplamKuyruk); /// Sıkıntııı
        System.out.println("Anlık Asansörde Bulunan Kişiler ve Gidecekleri Katlar: (" + toplamKuyruk + ",0)"); ///Sıkıntıııı
        while (!katKuyruk1.isEmpty()) {
            katKuyruk1.poll();
        }
        System.out.println("Mod: " + mode);
        System.out.println("Bulunulan Kat: 0");
        System.out.println("Yön: " + yon);
        System.out.println("Kapasite: " + kapasite);
         System.out.println("Asansördeki Kişi Sayısı: 0"); /// Sıkıntııı
        System.out.println("Anlık Asansörde Bulunan Kişiler ve Gidecekleri Katlar: (0,0)"); ///Sıkıntıııı
        
        System.out.println("Asansör En Alta İndi...");
    }

    @Override
    @SuppressWarnings("SleepWhileInLoop")
    public void run() {

        int k1 = 0;

        while (k1 < 10) {

            try {

                long start = System.currentTimeMillis();
                Thread.sleep(200);

                System.out.println("Uyunan zaman = " + (System.currentTimeMillis() - start));
            } catch (InterruptedException ex) {
                Logger.getLogger(Asansor1.class.getName()).log(Level.SEVERE, null, ex);
            }

            k1++;
        }
    }

}

class Asansor3 implements Runnable {

    // Katlardaki kuyruklari kontrol eder. En fazla 10 kisi alir. Müsterileri talep ettigi kata tasir. 
    boolean aktifMi = true;
    int hedefKat;
    String yon;
    final int kapasite = 10;

    int toplamKuyruktakiler;
    int sayac1 = 0, sayac2 = 0, sayac3 = 0, sayac4 = 0;
    int asansor1_inenler = 0;
    int kat;

    TumKatlar katlar = new TumKatlar();

    public BlockingQueue<Integer> kuyruk = new ArrayBlockingQueue<>(500);
    public BlockingQueue<Integer> katKuyruk1 = new ArrayBlockingQueue<>(500);
    public BlockingQueue<Integer> katKuyruk2 = new ArrayBlockingQueue<>(500);
    public BlockingQueue<Integer> katKuyruk3 = new ArrayBlockingQueue<>(500);
    public BlockingQueue<Integer> katKuyruk4 = new ArrayBlockingQueue<>(500);

    public BlockingQueue<Integer> hedefkat1 = new ArrayBlockingQueue<>(500);
    public BlockingQueue<Integer> hedefkat2 = new ArrayBlockingQueue<>(500);
    public BlockingQueue<Integer> hedefkat3 = new ArrayBlockingQueue<>(500);
    public BlockingQueue<Integer> hedefkat4 = new ArrayBlockingQueue<>(500);

    void AsansorYukari(BlockingQueue<Integer> hedefkat1, BlockingQueue<Integer> hedefkat2, BlockingQueue<Integer> hedefkat3, BlockingQueue<Integer> hedefkat4) {
        this.hedefkat1 = hedefkat1;
        this.hedefkat2 = hedefkat2;
        this.hedefkat3 = hedefkat3;
        this.hedefkat4 = hedefkat4;
    }

    void AsansorAsagi(BlockingQueue<Integer> katKuyruk1, BlockingQueue<Integer> katKuyruk2, BlockingQueue<Integer> katKuyruk3, BlockingQueue<Integer> katKuyruk4) {
        this.katKuyruk1 = katKuyruk1;
        this.katKuyruk2 = katKuyruk2;
        this.katKuyruk3 = katKuyruk3;
        this.katKuyruk4 = katKuyruk4;

    }

    public synchronized void YukariGit() {

        String mode = "çalışıyor";
        yon = "Yukarı";
        System.out.println("Aktiflik: " + aktifMi);
        int toplamKuyruk;
        toplamKuyruk = hedefkat1.size() + hedefkat2.size() + hedefkat3.size() + hedefkat4.size();

        System.out.println("Mod: " + mode);
        System.out.println("Bulunulan Kat: 0");
        System.out.println("Yön: " + yon);
        System.out.println("Kapasite: " + kapasite);
        if (toplamKuyruk < 10) {
            System.out.println("Asansördeki Kişi Sayısı: " + toplamKuyruk); /// Sıkıntııı
        } else {
            toplamKuyruk = 10;
            System.out.println("Asansördeki Kişi Sayısı: " + toplamKuyruk);
        }

        if (!hedefkat1.isEmpty()) {
            try {
                sleep(200);
            } catch (InterruptedException ex) {
                Logger.getLogger(Asansor1.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("Mod: " + mode);
            System.out.println("Bulunulan Kat: 1");
            System.out.println("Yön: " + yon);
            System.out.println("Kapasite: " + kapasite);
        }

        while (!hedefkat1.isEmpty()) {

            toplamKuyruk = hedefkat1.size() + hedefkat2.size() + hedefkat3.size() + hedefkat4.size();
            System.out.println("Asansördeki Kişi Sayısı: " + toplamKuyruk); /// Sıkıntııı
            System.out.println("Anlık Asansörde Bulunan Kişiler ve Gidecekleri Katlar: (" + hedefkat1.size() + ",1), (" + hedefkat2.size() + ",2), (" + hedefkat3.size() + ",3), (" + hedefkat4.size() + ",4)"); ///Sıkıntıııı
            hedefkat1.poll();

        }
        if (!hedefkat2.isEmpty()) {
            try {
                sleep(200);
            } catch (InterruptedException ex) {
                Logger.getLogger(Asansor1.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("Mod: " + mode);
            System.out.println("Bulunulan Kat: 2");
            System.out.println("Yön: " + yon);
            System.out.println("Kapasite: " + kapasite);
        }

        while (!hedefkat2.isEmpty()) {
            toplamKuyruk = hedefkat1.size() + hedefkat2.size() + hedefkat3.size() + hedefkat4.size();
            System.out.println("Asansördeki Kişi Sayısı: " + toplamKuyruk); /// Sıkıntııı

            System.out.println("Anlık Asansörde Bulunan Kişiler ve Gidecekleri Katlar: (" + hedefkat2.size() + ",2), (" + hedefkat3.size() + ",3), (" + hedefkat4.size() + ",4)"); ///Sıkıntıııı
            hedefkat2.poll();

        }
        if (!hedefkat3.isEmpty()) {
            try {
                sleep(200);
            } catch (InterruptedException ex) {
                Logger.getLogger(Asansor1.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("Mod: " + mode);
            System.out.println("Bulunulan Kat: 3");
            System.out.println("Yön: " + yon);
            System.out.println("Kapasite: " + kapasite);
        }

        while (!hedefkat3.isEmpty()) {
            toplamKuyruk = hedefkat1.size() + hedefkat2.size() + hedefkat3.size() + hedefkat4.size();
            System.out.println("Asansördeki Kişi Sayısı: " + toplamKuyruk); /// Sıkıntııı
            System.out.println("Anlık Asansörde Bulunan Kişiler ve Gidecekleri Katlar: (" + hedefkat3.size() + ",3), (" + hedefkat4.size() + ",4)"); ///Sıkıntıııı
            System.out.println("kuyruğun uzunluğu: " + hedefkat3.size());
            hedefkat3.poll();
        }
        if (!hedefkat4.isEmpty()) {
            try {
                sleep(200);
            } catch (InterruptedException ex) {
                Logger.getLogger(Asansor1.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("Mod: " + mode);
            System.out.println("Bulunulan Kat: 4");
            System.out.println("Yön: " + yon);
            System.out.println("Kapasite: " + kapasite);
        }

        while (!hedefkat4.isEmpty()) {
            toplamKuyruk = hedefkat1.size() + hedefkat2.size() + hedefkat3.size() + hedefkat4.size();
            System.out.println("Asansördeki Kişi Sayısı: " + toplamKuyruk); /// Sıkıntııı
            System.out.println("Anlık Asansörde Bulunan Kişiler ve Gidecekleri Katlar: (" + hedefkat4.size() + ",4)"); ///Sıkıntıııı
            hedefkat4.poll();
        }
        System.out.println("Asansör En Üste Çıktı...");
    }

    public synchronized void AsagiGit() {
        System.out.println("Asansör Aşağı İniyorrr.....");
        String mode = "çalışıyor";
        yon = "Aşağı";
        System.out.println("Aktiflik: " + aktifMi);
        int toplamKuyruk;
        toplamKuyruk = katKuyruk4.size();

        System.out.println("Mod: " + mode);
        System.out.println("Bulunulan Kat: 4");
        System.out.println("Yön: " + yon);
        System.out.println("Kapasite: " + kapasite);
        if (toplamKuyruk < 10) {
            System.out.println("Asansördeki Kişi Sayısı: " + toplamKuyruk); /// Sıkıntııı
        } else {
            toplamKuyruk = 10;
            System.out.println("Asansördeki Kişi Sayısı: " + toplamKuyruk);
        }
        toplamKuyruk = katKuyruk4.size();
        System.out.println("Asansördeki Kişi Sayısı: " + toplamKuyruk); /// Sıkıntııı
        System.out.println("Anlık Asansörde Bulunan Kişiler ve Gidecekleri Katlar: (" + toplamKuyruk + ",0)"); ///Sıkıntıııı

        while (!katKuyruk4.isEmpty()) {
            katKuyruk4.poll(); // sil
        }
        try {
            sleep(200);
        } catch (InterruptedException ex) {
            Logger.getLogger(Asansor1.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (!katKuyruk3.isEmpty()) {

            System.out.println("Mod: " + mode);
            System.out.println("Bulunulan Kat: 3");
            System.out.println("Yön: " + yon);
            System.out.println("Kapasite: " + kapasite);
        }
        toplamKuyruk += katKuyruk3.size();
        System.out.println("Asansördeki Kişi Sayısı: " + toplamKuyruk); /// Sıkıntııı
        System.out.println("Anlık Asansörde Bulunan Kişiler ve Gidecekleri Katlar: (" + toplamKuyruk + ",0)"); ///Sıkıntıııı
        while (!katKuyruk3.isEmpty()) {
            katKuyruk3.poll();
        }
        try {
            sleep(200);
        } catch (InterruptedException ex) {
            Logger.getLogger(Asansor1.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (!katKuyruk2.isEmpty()) {
            System.out.println("Mod: " + mode);
            System.out.println("Bulunulan Kat: 2");
            System.out.println("Yön: " + yon);
            System.out.println("Kapasite: " + kapasite);
        }
        toplamKuyruk += katKuyruk2.size();
        while (!katKuyruk2.isEmpty()) {
            System.out.println("Asansördeki Kişi Sayısı: " + toplamKuyruk); /// Sıkıntııı
            System.out.println("Anlık Asansörde Bulunan Kişiler ve Gidecekleri Katlar: (" + toplamKuyruk + ",0)"); ///Sıkıntıııı
            katKuyruk2.poll();
        }
        try {
            sleep(200);
        } catch (InterruptedException ex) {
            Logger.getLogger(Asansor1.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (!katKuyruk1.isEmpty()) {

            System.out.println("Mod: " + mode);
            System.out.println("Bulunulan Kat: 1");
            System.out.println("Yön: " + yon);
            System.out.println("Kapasite: " + kapasite);
        }
        toplamKuyruk += katKuyruk1.size();

        System.out.println("Asansördeki Kişi Sayısı: " + toplamKuyruk); /// Sıkıntııı
        System.out.println("Anlık Asansörde Bulunan Kişiler ve Gidecekleri Katlar: (" + toplamKuyruk + ",0)"); ///Sıkıntıııı
        while (!katKuyruk1.isEmpty()) {
            katKuyruk1.poll();
        }
        System.out.println("Mod: " + mode);
        System.out.println("Bulunulan Kat: 0");
        System.out.println("Yön: " + yon);
        System.out.println("Kapasite: " + kapasite);
         System.out.println("Asansördeki Kişi Sayısı: 0"); /// Sıkıntııı
        System.out.println("Anlık Asansörde Bulunan Kişiler ve Gidecekleri Katlar: (0,0)"); ///Sıkıntıııı
        
        System.out.println("Asansör En Alta İndi...");
    }

    @Override
    @SuppressWarnings("SleepWhileInLoop")
    public void run() {

        int k1 = 0;

        while (k1 < 10) {

            try {

                long start = System.currentTimeMillis();
                Thread.sleep(200);

                System.out.println("Uyunan zaman = " + (System.currentTimeMillis() - start));
            } catch (InterruptedException ex) {
                Logger.getLogger(Asansor1.class.getName()).log(Level.SEVERE, null, ex);
            }

            k1++;
        }
    }
}

class Asansor4 implements Runnable {

    // Katlardaki kuyruklari kontrol eder. En fazla 10 kisi alir. Müsterileri talep ettigi kata tasir. 
    boolean aktifMi = true;
    int hedefKat;
    String yon;
    final int kapasite = 10;

    int toplamKuyruktakiler;
    int sayac1 = 0, sayac2 = 0, sayac3 = 0, sayac4 = 0;
    int asansor1_inenler = 0;
    int kat;

    TumKatlar katlar = new TumKatlar();

    public BlockingQueue<Integer> kuyruk = new ArrayBlockingQueue<>(500);
    public BlockingQueue<Integer> katKuyruk1 = new ArrayBlockingQueue<>(500);
    public BlockingQueue<Integer> katKuyruk2 = new ArrayBlockingQueue<>(500);
    public BlockingQueue<Integer> katKuyruk3 = new ArrayBlockingQueue<>(500);
    public BlockingQueue<Integer> katKuyruk4 = new ArrayBlockingQueue<>(500);

    public BlockingQueue<Integer> hedefkat1 = new ArrayBlockingQueue<>(500);
    public BlockingQueue<Integer> hedefkat2 = new ArrayBlockingQueue<>(500);
    public BlockingQueue<Integer> hedefkat3 = new ArrayBlockingQueue<>(500);
    public BlockingQueue<Integer> hedefkat4 = new ArrayBlockingQueue<>(500);

    void AsansorYukari(BlockingQueue<Integer> hedefkat1, BlockingQueue<Integer> hedefkat2, BlockingQueue<Integer> hedefkat3, BlockingQueue<Integer> hedefkat4) {
        this.hedefkat1 = hedefkat1;
        this.hedefkat2 = hedefkat2;
        this.hedefkat3 = hedefkat3;
        this.hedefkat4 = hedefkat4;
    }

    void AsansorAsagi(BlockingQueue<Integer> katKuyruk1, BlockingQueue<Integer> katKuyruk2, BlockingQueue<Integer> katKuyruk3, BlockingQueue<Integer> katKuyruk4) {
        this.katKuyruk1 = katKuyruk1;
        this.katKuyruk2 = katKuyruk2;
        this.katKuyruk3 = katKuyruk3;
        this.katKuyruk4 = katKuyruk4;

    }

    public synchronized void YukariGit() {

        String mode = "çalışıyor";
        yon = "Yukarı";
        System.out.println("Aktiflik: " + aktifMi);
        int toplamKuyruk;
        toplamKuyruk = hedefkat1.size() + hedefkat2.size() + hedefkat3.size() + hedefkat4.size();

        System.out.println("Mod: " + mode);
        System.out.println("Bulunulan Kat: 0");
        System.out.println("Yön: " + yon);
        System.out.println("Kapasite: " + kapasite);
        if (toplamKuyruk < 10) {
            System.out.println("Asansördeki Kişi Sayısı: " + toplamKuyruk); /// Sıkıntııı
        } else {
            toplamKuyruk = 10;
            System.out.println("Asansördeki Kişi Sayısı: " + toplamKuyruk);
        }

        if (!hedefkat1.isEmpty()) {
            try {
                sleep(200);
            } catch (InterruptedException ex) {
                Logger.getLogger(Asansor1.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("Mod: " + mode);
            System.out.println("Bulunulan Kat: 1");
            System.out.println("Yön: " + yon);
            System.out.println("Kapasite: " + kapasite);
        }

        while (!hedefkat1.isEmpty()) {

            toplamKuyruk = hedefkat1.size() + hedefkat2.size() + hedefkat3.size() + hedefkat4.size();
            System.out.println("Asansördeki Kişi Sayısı: " + toplamKuyruk); /// Sıkıntııı
            System.out.println("Anlık Asansörde Bulunan Kişiler ve Gidecekleri Katlar: (" + hedefkat1.size() + ",1), (" + hedefkat2.size() + ",2), (" + hedefkat3.size() + ",3), (" + hedefkat4.size() + ",4)"); ///Sıkıntıııı
            hedefkat1.poll();

        }
        if (!hedefkat2.isEmpty()) {
            try {
                sleep(200);
            } catch (InterruptedException ex) {
                Logger.getLogger(Asansor1.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("Mod: " + mode);
            System.out.println("Bulunulan Kat: 2");
            System.out.println("Yön: " + yon);
            System.out.println("Kapasite: " + kapasite);
        }

        while (!hedefkat2.isEmpty()) {
            toplamKuyruk = hedefkat1.size() + hedefkat2.size() + hedefkat3.size() + hedefkat4.size();
            System.out.println("Asansördeki Kişi Sayısı: " + toplamKuyruk); /// Sıkıntııı

            System.out.println("Anlık Asansörde Bulunan Kişiler ve Gidecekleri Katlar: (" + hedefkat2.size() + ",2), (" + hedefkat3.size() + ",3), (" + hedefkat4.size() + ",4)"); ///Sıkıntıııı
            hedefkat2.poll();

        }
        if (!hedefkat3.isEmpty()) {
            try {
                sleep(200);
            } catch (InterruptedException ex) {
                Logger.getLogger(Asansor1.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("Mod: " + mode);
            System.out.println("Bulunulan Kat: 3");
            System.out.println("Yön: " + yon);
            System.out.println("Kapasite: " + kapasite);
        }

        while (!hedefkat3.isEmpty()) {
            toplamKuyruk = hedefkat1.size() + hedefkat2.size() + hedefkat3.size() + hedefkat4.size();
            System.out.println("Asansördeki Kişi Sayısı: " + toplamKuyruk); /// Sıkıntııı
            System.out.println("Anlık Asansörde Bulunan Kişiler ve Gidecekleri Katlar: (" + hedefkat3.size() + ",3), (" + hedefkat4.size() + ",4)"); ///Sıkıntıııı
            System.out.println("kuyruğun uzunluğu: " + hedefkat3.size());
            hedefkat3.poll();
        }
        if (!hedefkat4.isEmpty()) {
            try {
                sleep(200);
            } catch (InterruptedException ex) {
                Logger.getLogger(Asansor1.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("Mod: " + mode);
            System.out.println("Bulunulan Kat: 4");
            System.out.println("Yön: " + yon);
            System.out.println("Kapasite: " + kapasite);
        }

        while (!hedefkat4.isEmpty()) {
            toplamKuyruk = hedefkat1.size() + hedefkat2.size() + hedefkat3.size() + hedefkat4.size();
            System.out.println("Asansördeki Kişi Sayısı: " + toplamKuyruk); /// Sıkıntııı
            System.out.println("Anlık Asansörde Bulunan Kişiler ve Gidecekleri Katlar: (" + hedefkat4.size() + ",4)"); ///Sıkıntıııı
            hedefkat4.poll();
        }
        System.out.println("Asansör En Üste Çıktı...");
    }

    public synchronized void AsagiGit() {
        System.out.println("Asansör Aşağı İniyorrr.....");
        String mode = "çalışıyor";
        yon = "Aşağı";
        System.out.println("Aktiflik: " + aktifMi);
        int toplamKuyruk;
        toplamKuyruk = katKuyruk4.size();

        System.out.println("Mod: " + mode);
        System.out.println("Bulunulan Kat: 4");
        System.out.println("Yön: " + yon);
        System.out.println("Kapasite: " + kapasite);
        if (toplamKuyruk < 10) {
            System.out.println("Asansördeki Kişi Sayısı: " + toplamKuyruk); /// Sıkıntııı
        } else {
            toplamKuyruk = 10;
            System.out.println("Asansördeki Kişi Sayısı: " + toplamKuyruk);
        }
        toplamKuyruk = katKuyruk4.size();
        System.out.println("Asansördeki Kişi Sayısı: " + toplamKuyruk); /// Sıkıntııı
        System.out.println("Anlık Asansörde Bulunan Kişiler ve Gidecekleri Katlar: (" + toplamKuyruk + ",0)"); ///Sıkıntıııı

        while (!katKuyruk4.isEmpty()) {
            katKuyruk4.poll(); // sil
        }
        try {
            sleep(200);
        } catch (InterruptedException ex) {
            Logger.getLogger(Asansor1.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (!katKuyruk3.isEmpty()) {

            System.out.println("Mod: " + mode);
            System.out.println("Bulunulan Kat: 3");
            System.out.println("Yön: " + yon);
            System.out.println("Kapasite: " + kapasite);
        }
        toplamKuyruk += katKuyruk3.size();
        System.out.println("Asansördeki Kişi Sayısı: " + toplamKuyruk); /// Sıkıntııı
        System.out.println("Anlık Asansörde Bulunan Kişiler ve Gidecekleri Katlar: (" + toplamKuyruk + ",0)"); ///Sıkıntıııı
        while (!katKuyruk3.isEmpty()) {
            katKuyruk3.poll();
        }
        try {
            sleep(200);
        } catch (InterruptedException ex) {
            Logger.getLogger(Asansor1.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (!katKuyruk2.isEmpty()) {
            System.out.println("Mod: " + mode);
            System.out.println("Bulunulan Kat: 2");
            System.out.println("Yön: " + yon);
            System.out.println("Kapasite: " + kapasite);
        }
        toplamKuyruk += katKuyruk2.size();
        while (!katKuyruk2.isEmpty()) {
            System.out.println("Asansördeki Kişi Sayısı: " + toplamKuyruk); /// Sıkıntııı
            System.out.println("Anlık Asansörde Bulunan Kişiler ve Gidecekleri Katlar: (" + toplamKuyruk + ",0)"); ///Sıkıntıııı
            katKuyruk2.poll();
        }
        try {
            sleep(200);
        } catch (InterruptedException ex) {
            Logger.getLogger(Asansor1.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (!katKuyruk1.isEmpty()) {

            System.out.println("Mod: " + mode);
            System.out.println("Bulunulan Kat: 1");
            System.out.println("Yön: " + yon);
            System.out.println("Kapasite: " + kapasite);
        }
        toplamKuyruk += katKuyruk1.size();

        System.out.println("Asansördeki Kişi Sayısı: " + toplamKuyruk); /// Sıkıntııı
        System.out.println("Anlık Asansörde Bulunan Kişiler ve Gidecekleri Katlar: (" + toplamKuyruk + ",0)"); ///Sıkıntıııı
        while (!katKuyruk1.isEmpty()) {
            katKuyruk1.poll();
        }
        System.out.println("Mod: " + mode);
        System.out.println("Bulunulan Kat: 0");
        System.out.println("Yön: " + yon);
        System.out.println("Kapasite: " + kapasite);
         System.out.println("Asansördeki Kişi Sayısı: 0"); /// Sıkıntııı
        System.out.println("Anlık Asansörde Bulunan Kişiler ve Gidecekleri Katlar: (0,0)"); ///Sıkıntıııı
        
        System.out.println("Asansör En Alta İndi...");
    }

    @Override
    @SuppressWarnings("SleepWhileInLoop")
    public void run() {

        int k1 = 0;

        while (k1 < 10) {

            try {

                long start = System.currentTimeMillis();
                Thread.sleep(200);

                System.out.println("Uyunan zaman = " + (System.currentTimeMillis() - start));
            } catch (InterruptedException ex) {
                Logger.getLogger(Asansor1.class.getName()).log(Level.SEVERE, null, ex);
            }

            k1++;
        }
    }
}

class Asansor5 implements Runnable {

    // Katlardaki kuyruklari kontrol eder. En fazla 10 kisi alir. Müsterileri talep ettigi kata tasir. 
    boolean aktifMi = true;
    int hedefKat;
    String yon;
    final int kapasite = 10;

    int toplamKuyruktakiler;
    int sayac1 = 0, sayac2 = 0, sayac3 = 0, sayac4 = 0;
    int asansor1_inenler = 0;
    int kat;

    TumKatlar katlar = new TumKatlar();

    public BlockingQueue<Integer> kuyruk = new ArrayBlockingQueue<>(500);
    public BlockingQueue<Integer> katKuyruk1 = new ArrayBlockingQueue<>(500);
    public BlockingQueue<Integer> katKuyruk2 = new ArrayBlockingQueue<>(500);
    public BlockingQueue<Integer> katKuyruk3 = new ArrayBlockingQueue<>(500);
    public BlockingQueue<Integer> katKuyruk4 = new ArrayBlockingQueue<>(500);

    public BlockingQueue<Integer> hedefkat1 = new ArrayBlockingQueue<>(500);
    public BlockingQueue<Integer> hedefkat2 = new ArrayBlockingQueue<>(500);
    public BlockingQueue<Integer> hedefkat3 = new ArrayBlockingQueue<>(500);
    public BlockingQueue<Integer> hedefkat4 = new ArrayBlockingQueue<>(500);

    void AsansorYukari(BlockingQueue<Integer> hedefkat1, BlockingQueue<Integer> hedefkat2, BlockingQueue<Integer> hedefkat3, BlockingQueue<Integer> hedefkat4) {
        this.hedefkat1 = hedefkat1;
        this.hedefkat2 = hedefkat2;
        this.hedefkat3 = hedefkat3;
        this.hedefkat4 = hedefkat4;
    }

    void AsansorAsagi(BlockingQueue<Integer> katKuyruk1, BlockingQueue<Integer> katKuyruk2, BlockingQueue<Integer> katKuyruk3, BlockingQueue<Integer> katKuyruk4) {
        this.katKuyruk1 = katKuyruk1;
        this.katKuyruk2 = katKuyruk2;
        this.katKuyruk3 = katKuyruk3;
        this.katKuyruk4 = katKuyruk4;

    }

    public synchronized void YukariGit() {

        String mode = "çalışıyor";
        yon = "Yukarı";
        System.out.println("Aktiflik: " + aktifMi);
        int toplamKuyruk;
        toplamKuyruk = hedefkat1.size() + hedefkat2.size() + hedefkat3.size() + hedefkat4.size();

        System.out.println("Mod: " + mode);
        System.out.println("Bulunulan Kat: 0");
        System.out.println("Yön: " + yon);
        System.out.println("Kapasite: " + kapasite);
        if (toplamKuyruk < 10) {
            System.out.println("Asansördeki Kişi Sayısı: " + toplamKuyruk); /// Sıkıntııı
        } else {
            toplamKuyruk = 10;
            System.out.println("Asansördeki Kişi Sayısı: " + toplamKuyruk);
        }

        if (!hedefkat1.isEmpty()) {
            try {
                sleep(200);
            } catch (InterruptedException ex) {
                Logger.getLogger(Asansor1.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("Mod: " + mode);
            System.out.println("Bulunulan Kat: 1");
            System.out.println("Yön: " + yon);
            System.out.println("Kapasite: " + kapasite);
        }

        while (!hedefkat1.isEmpty()) {

            toplamKuyruk = hedefkat1.size() + hedefkat2.size() + hedefkat3.size() + hedefkat4.size();
            System.out.println("Asansördeki Kişi Sayısı: " + toplamKuyruk); /// Sıkıntııı
            System.out.println("Anlık Asansörde Bulunan Kişiler ve Gidecekleri Katlar: (" + hedefkat1.size() + ",1), (" + hedefkat2.size() + ",2), (" + hedefkat3.size() + ",3), (" + hedefkat4.size() + ",4)"); ///Sıkıntıııı
            hedefkat1.poll();

        }
        if (!hedefkat2.isEmpty()) {
            try {
                sleep(200);
            } catch (InterruptedException ex) {
                Logger.getLogger(Asansor1.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("Mod: " + mode);
            System.out.println("Bulunulan Kat: 2");
            System.out.println("Yön: " + yon);
            System.out.println("Kapasite: " + kapasite);
        }

        while (!hedefkat2.isEmpty()) {
            toplamKuyruk = hedefkat1.size() + hedefkat2.size() + hedefkat3.size() + hedefkat4.size();
            System.out.println("Asansördeki Kişi Sayısı: " + toplamKuyruk); /// Sıkıntııı

            System.out.println("Anlık Asansörde Bulunan Kişiler ve Gidecekleri Katlar: (" + hedefkat2.size() + ",2), (" + hedefkat3.size() + ",3), (" + hedefkat4.size() + ",4)"); ///Sıkıntıııı
            hedefkat2.poll();

        }
        if (!hedefkat3.isEmpty()) {
            try {
                sleep(200);
            } catch (InterruptedException ex) {
                Logger.getLogger(Asansor1.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("Mod: " + mode);
            System.out.println("Bulunulan Kat: 3");
            System.out.println("Yön: " + yon);
            System.out.println("Kapasite: " + kapasite);
        }

        while (!hedefkat3.isEmpty()) {
            toplamKuyruk = hedefkat1.size() + hedefkat2.size() + hedefkat3.size() + hedefkat4.size();
            System.out.println("Asansördeki Kişi Sayısı: " + toplamKuyruk); /// Sıkıntııı
            System.out.println("Anlık Asansörde Bulunan Kişiler ve Gidecekleri Katlar: (" + hedefkat3.size() + ",3), (" + hedefkat4.size() + ",4)"); ///Sıkıntıııı
            System.out.println("kuyruğun uzunluğu: " + hedefkat3.size());
            hedefkat3.poll();
        }
        if (!hedefkat4.isEmpty()) {
            try {
                sleep(200);
            } catch (InterruptedException ex) {
                Logger.getLogger(Asansor1.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("Mod: " + mode);
            System.out.println("Bulunulan Kat: 4");
            System.out.println("Yön: " + yon);
            System.out.println("Kapasite: " + kapasite);
        }

        while (!hedefkat4.isEmpty()) {
            toplamKuyruk = hedefkat1.size() + hedefkat2.size() + hedefkat3.size() + hedefkat4.size();
            System.out.println("Asansördeki Kişi Sayısı: " + toplamKuyruk); /// Sıkıntııı
            System.out.println("Anlık Asansörde Bulunan Kişiler ve Gidecekleri Katlar: (" + hedefkat4.size() + ",4)"); ///Sıkıntıııı
            hedefkat4.poll();
        }
        System.out.println("Asansör En Üste Çıktı...");
    }

    public synchronized void AsagiGit() {
        System.out.println("Asansör Aşağı İniyorrr.....");
        String mode = "çalışıyor";
        yon = "Aşağı";
        System.out.println("Aktiflik: " + aktifMi);
        int toplamKuyruk;
        toplamKuyruk = katKuyruk4.size();

        System.out.println("Mod: " + mode);
        System.out.println("Bulunulan Kat: 4");
        System.out.println("Yön: " + yon);
        System.out.println("Kapasite: " + kapasite);
        if (toplamKuyruk < 10) {
            System.out.println("Asansördeki Kişi Sayısı: " + toplamKuyruk); /// Sıkıntııı
        } else {
            toplamKuyruk = 10;
            System.out.println("Asansördeki Kişi Sayısı: " + toplamKuyruk);
        }
        toplamKuyruk = katKuyruk4.size();
        System.out.println("Asansördeki Kişi Sayısı: " + toplamKuyruk); /// Sıkıntııı
        System.out.println("Anlık Asansörde Bulunan Kişiler ve Gidecekleri Katlar: (" + toplamKuyruk + ",0)"); ///Sıkıntıııı

        while (!katKuyruk4.isEmpty()) {
            katKuyruk4.poll(); // sil
        }
        try {
            sleep(200);
        } catch (InterruptedException ex) {
            Logger.getLogger(Asansor1.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (!katKuyruk3.isEmpty()) {

            System.out.println("Mod: " + mode);
            System.out.println("Bulunulan Kat: 3");
            System.out.println("Yön: " + yon);
            System.out.println("Kapasite: " + kapasite);
        }
        toplamKuyruk += katKuyruk3.size();
        System.out.println("Asansördeki Kişi Sayısı: " + toplamKuyruk); /// Sıkıntııı
        System.out.println("Anlık Asansörde Bulunan Kişiler ve Gidecekleri Katlar: (" + toplamKuyruk + ",0)"); ///Sıkıntıııı
        while (!katKuyruk3.isEmpty()) {
            katKuyruk3.poll();
        }
        try {
            sleep(200);
        } catch (InterruptedException ex) {
            Logger.getLogger(Asansor1.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (!katKuyruk2.isEmpty()) {
            System.out.println("Mod: " + mode);
            System.out.println("Bulunulan Kat: 2");
            System.out.println("Yön: " + yon);
            System.out.println("Kapasite: " + kapasite);
        }
        toplamKuyruk += katKuyruk2.size();
        while (!katKuyruk2.isEmpty()) {
            System.out.println("Asansördeki Kişi Sayısı: " + toplamKuyruk); /// Sıkıntııı
            System.out.println("Anlık Asansörde Bulunan Kişiler ve Gidecekleri Katlar: (" + toplamKuyruk + ",0)"); ///Sıkıntıııı
            katKuyruk2.poll();
        }
        try {
            sleep(200);
        } catch (InterruptedException ex) {
            Logger.getLogger(Asansor1.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (!katKuyruk1.isEmpty()) {

            System.out.println("Mod: " + mode);
            System.out.println("Bulunulan Kat: 1");
            System.out.println("Yön: " + yon);
            System.out.println("Kapasite: " + kapasite);
        }
        toplamKuyruk += katKuyruk1.size();

        System.out.println("Asansördeki Kişi Sayısı: " + toplamKuyruk); /// Sıkıntııı
        System.out.println("Anlık Asansörde Bulunan Kişiler ve Gidecekleri Katlar: (" + toplamKuyruk + ",0)"); ///Sıkıntıııı
        while (!katKuyruk1.isEmpty()) {
            katKuyruk1.poll();
        }
        System.out.println("Mod: " + mode);
        System.out.println("Bulunulan Kat: 0");
        System.out.println("Yön: " + yon);
        System.out.println("Kapasite: " + kapasite);
         System.out.println("Asansördeki Kişi Sayısı: 0"); /// Sıkıntııı
        System.out.println("Anlık Asansörde Bulunan Kişiler ve Gidecekleri Katlar: (0,0)"); ///Sıkıntıııı
        
        System.out.println("Asansör En Alta İndi...");
    }

    @Override
    @SuppressWarnings("SleepWhileInLoop")
    public void run() {

        int k1 = 0;

        while (k1 < 10) {

            try {

                long start = System.currentTimeMillis();
                Thread.sleep(200);

                System.out.println("Uyunan zaman = " + (System.currentTimeMillis() - start));
            } catch (InterruptedException ex) {
                Logger.getLogger(Asansor1.class.getName()).log(Level.SEVERE, null, ex);
            }

            k1++;
        }
    }
}

class ThreadRenkler {

    public static final String Black = "\u001b[30m";
    public static final String Red = "\u001b[31m";
    public static final String Green = "\u001b[32m";
    public static final String Yellow = "\u001b[33m";
    public static final String Blue = "\u001b[34m";
    public static final String Magenta = "\u001b[35m";
    public static final String Cyan = "\u001b[36m";
    public static final String White = "\u001b[37m";
    public static final String Reset = "\u001b[0m";
}
