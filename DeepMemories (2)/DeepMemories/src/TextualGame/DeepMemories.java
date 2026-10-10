package TextualGame;

import java.util.Scanner;

public class DeepMemories {

    // Quantas vezes a personagem já morreu
    static int mortes = 0;

    // Regras do renascimento (mudem aqui se ficar difícil ou fácil demais)
    static final double PERDA_VIDA_MAXIMA = 0.10;   // perde 10% da vida máxima a cada morte
    static final double VIDA_AO_RENASCER = 0.50;    // volta com 50% da vida máxima
    static final int VIDA_MAXIMA_MINIMA = 50;       // a vida máxima nunca cai abaixo disso

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        Personagem personagem = new Personagem();
        Inventario inventario = new Inventario();
        MenuCombate menuCombate = new MenuCombate();
        Bau bau = new Bau();

        personagem.pName = "Amanda";

        // Item inicial (opcional)
        inventario.adicionarItem(new Item("Espada de cobre", "dano", 22));

        //Exemplo de como usar o lutarAteVencer (Lutar com um boss, se a personagem morrer ela renasce com o hp nerfado e fica na luta ate ganhar
        lutarAteVencer(sc, personagem, inventario, menuCombate, "Eco da Culpa", 50, 8);
        //Scanner, personagem, inventario, menuCombate, Nome do inimigo, Vida do inimigo, Dano do inimigo
    }

    // ===== 4. COMBATE COM RENASCIMENTO (não precisa mexer) =====

    // Repete a luta até a personagem vencer. Se ela morrer, renasce mais fraca.

    public static void lutarAteVencer(Scanner sc, Personagem personagem, Inventario inventario, MenuCombate menuCombate, String nome, int vida, int dano) {

        boolean venceu = false;

        while (!venceu) {
            Inimigo inimigo = new Inimigo(nome, vida, dano);   // inimigo novo a cada tentativa
            menuCombate.iniciarCombate(personagem, inimigo, inventario, sc);

            if (personagem.pHp <= 0) {
                renascer(personagem);
            } else if (inimigo.hp <= 0) {
                venceu = true;
            } else {
                System.out.println("Não há para onde fugir. O medo te alcança de novo...");
            }
        }
    }

    // Penalidade da morte: perde vida máxima e volta com parte da vida
    public static void renascer(Personagem personagem) {
        mortes++;

        System.out.println("");
        System.out.println("Tudo escurece... mas a fonte não deixa você partir.");

        personagem.pMaxHp = Math.max(VIDA_MAXIMA_MINIMA, (int) (personagem.pMaxHp * (1 - PERDA_VIDA_MAXIMA)));
        personagem.pHp = Math.max(1, (int) (personagem.pMaxHp * VIDA_AO_RENASCER));

        System.out.println("Você acorda mais fraca. Vida: " + personagem.pHp + "/" + personagem.pMaxHp);
    }

    // PAUSA ENTRE TEXTOS (para o jogador ler com calma)
    public static void pausar(Scanner sc) {
        System.out.println("(pressione ENTER para continuar)");
        sc.nextLine();
    }
}