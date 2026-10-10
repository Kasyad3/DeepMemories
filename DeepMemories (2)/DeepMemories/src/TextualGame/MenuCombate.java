package TextualGame;

import java.util.concurrent.TimeUnit;
import java.util.Scanner;

public class MenuCombate {

    private static final double CHANCE_FUGA = 0.2;

    public void iniciarCombate(Personagem personagem, Inimigo inimigo, Inventario inventario, Scanner sc) {

        boolean emCombate = true;
        System.out.println("O inimigo " + inimigo.nome + " está no seu caminho!");

        while (emCombate && personagem.pHp > 0 && inimigo.hp > 0) {

            System.out.println("\n---- Seu HP: " + personagem.pHp + " | " + inimigo.nome + " HP: " + inimigo.hp);
            System.out.println("1. Atacar | 2. Item | 3. Fugir");

            int escolha = lerInt(sc);
            boolean gastouTurno = true;

            if (escolha == 1) {
                inimigo.hp -= personagem.pAttk;
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                System.out.println("Você causou " + personagem.pAttk + " de dano em " + inimigo.nome + "!");

            } else if (escolha == 2) {
                gastouTurno = inventario.menuUsarItem(sc, personagem, inimigo);

            } else if (escolha == 3) {
                if (Math.random() < CHANCE_FUGA) {
                    System.out.println("Você conseguiu fugir!");
                    emCombate = false;
                } else {
                    System.out.println("\nO inimigo puxou você pelas pernas e jogou de volta para a Arena!\n");
                }

            } else {
                System.out.println("Opção inválida.");
                gastouTurno = false;
            }

            if (!gastouTurno) {
                continue;
            }

            if (emCombate && inimigo.hp <= 0) {
                System.out.println(inimigo.nome + " foi derrotado!");
                emCombate = false;

            } else if (emCombate) {
                personagem.pHp = Math.max(0, personagem.pHp - inimigo.dano);
                System.out.println("\n" + inimigo.nome + " te atacou e causou " + inimigo.dano + " de dano!");

                inventario.atualizarBuffs(personagem);
            }
        }

        inimigo.hp = Math.max(0, inimigo.hp);
        inventario.limparBuffs(personagem);
    }

    private int lerInt(Scanner sc) {
        try {
            return Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}