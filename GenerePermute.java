//
//LUCAS GOURLIA Devoir 3
//
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class GenerePermute {
    public static final String[] LETTRES = { "A", "B", "C", "D", "E", "F" };
    public static final int TAILLE = LETTRES.length;

    public static ArrayList<ArrayList<String>> genereCombine(int longueur, boolean rep) {
        ArrayList<ArrayList<String>> res = new ArrayList<>();
        for (int i = 0; i < Math.pow(TAILLE, longueur); i++) {
            ArrayList<Integer> tab = intToArray(i, longueur);
            ArrayList<String> candidat = new ArrayList<>();
            for (int k : tab) {
                candidat.add(LETTRES[k]);
            }
            if (rep || hasNoRepetition(candidat)) {
                res.add(candidat);
            }
        }
        return res;
    }

    public static ArrayList<Integer> intToArray(int n, int longMot) {
        ArrayList<Integer> res = new ArrayList<>(longMot);
        for (int i = 0; i < longMot; i++) {
            int q = (int) Math.pow(TAILLE, longMot - i - 1);
            int r = n / q;
            res.add(r);
            n %= q;
        }
        return res;
    }

    public static boolean hasNoRepetition(ArrayList<String> candidat) {
        ArrayList<Integer> iCount = new ArrayList<>(TAILLE);
        for (int i = 0; i < TAILLE; i++) {
            iCount.add(0);
        }
        for (String c : candidat) {
            int index = indexOf(c);
            if (iCount.get(index) == 1) {
                return false;
            }
            iCount.set(index, 1);
        }
        return true;
    }

    public static int indexOf(String lettre) {
        for (int i = 0; i < LETTRES.length; i++) {
            if (LETTRES[i].equals(lettre)) {
                return i;
            }
        }
        return -1;
    }

    public static ArrayList<String> randomChoice(ArrayList<ArrayList<String>> candidats) {
        Random rand = new Random();
        int index = rand.nextInt(candidats.size());
        return candidats.get(index);
    }

    public static Paire compare(ArrayList<String> candidat, ArrayList<String> essai) {
        int bonnePlace = 0, mauvaisePlace = 0;
        ArrayList<String> copyCode = new ArrayList<>(essai);
        ArrayList<String> copyCandidat = new ArrayList<>(candidat);
        int le = essai.size();
        for (int i = 0; i < le; i++) {
            if (copyCode.get(i).equals(copyCandidat.get(i))) {
                bonnePlace++;
                copyCode.set(i, "@");
                copyCandidat.set(i, "*");
            }
        }
        for (int i = 0; i < le; i++) {
            if (copyCode.get(i).equals("@")) {
                continue;
            }
            for (int j = 0; j < le; j++) {
                if (copyCode.get(i).equals(copyCandidat.get(j))) {
                    mauvaisePlace++;
                    copyCandidat.set(j, "*");
                    break;
                }
            }
        }
        return new Paire(bonnePlace, mauvaisePlace);
    }

    public static ArrayList<ArrayList<String>> filterCandidats(ArrayList<ArrayList<String>> candidats,
            ArrayList<String> essai, Paire score) {
        ArrayList<ArrayList<String>> res = new ArrayList<>();
        for (ArrayList<String> candidat : candidats) {
            if (compare(candidat, essai).equals(score)) {
                res.add(candidat);
            }
        }
        return res;
    }

    public static Paire readScore(Scanner sc) {
        int bb = sc.nextInt();
        int bm = sc.nextInt();
        return new Paire(bb, bm);
    }

    public static class Paire {
        int a, b;

        public Paire(int a, int b) {
            this.a = a;
            this.b = b;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Paire) {
                Paire other = (Paire) obj;
                return this.a == other.a && this.b == other.b;
            }
            return false;
        }

        @Override
        public String toString() {
            return "(" + a + ", " + b + ")";
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Bienvenue au jeu où l'ordinateur tente de trouver le code secret");
        System.out.println("Le code secret est un mot fait des lettres suivantes: " + String.join(", ", LETTRES));
        System.out.println("Combien de lettres (entre 2 et 5) le code secret a-t-il ?");
        int n = sc.nextInt();
        if (n < 2 || n > 5) {
            System.out.println("nombre invalide");
            sc.close();
            return;
        }
        System.out.println("Est-ce que les lettres peuvent se répéter dans le code secret ? (oui/non)");
        boolean rep = sc.next().trim().equalsIgnoreCase("oui");
        ArrayList<ArrayList<String>> candidats = genereCombine(n, rep);
        while (true) {
            System.out.println("Nombre de candidats: " + candidats.size());
            ArrayList<String> essai = randomChoice(candidats);
            System.out.println("Voici mon essai: " + String.join("", essai));
            System.out.println("Combien de lettres sont en bonnes positions et combien sont bonnes mais mal placées");
            Paire score = readScore(sc);
            if (score.a == n) {
                System.out.println("Youppi!!");
                break;
            }
            candidats = filterCandidats(candidats, essai, score);
            if (candidats.isEmpty()) {
                System.out.println("Il n'y a plus de candidats! Vous avez triché!");
                break;
            }
        }
    }
}