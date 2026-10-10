package TextualGame;

import java.util.ArrayList;
import java.util.Scanner;

public class Inventario {

   //NÃO MUDEM ISSO, A DURAÇÃO DO BUFF É 4 PORQUE CONTA QUANDO USA O ITEM, ENTÃO NO FINAL DAS CONTAS TODOS OS BUFFS DURAM 3 TURNOS//
    private static final int DURACAO_BUFF = 4;

    ArrayList<Item> itens = new ArrayList<>();

    public void adicionarItem(Item item) {
        itens.add(item);
    }

    public boolean temItens() {
        return !itens.isEmpty();
    }

    public void mostrarInventario() {
        System.out.println("===== INVENTÁRIO =====");

        if (itens.isEmpty()) {
            System.out.println("Seu inventário está vazio.");
            return;
        }

        for (int i = 0; i < itens.size(); i++) {
            System.out.println((i + 1) + " - " + itens.get(i).nome);
        }
    }

    public void removerItem(int escolha) {
        if (escolha >= 1 && escolha <= itens.size()) {
            Item itemRemovido = itens.remove(escolha - 1);
            System.out.println(itemRemovido.nome + " foi removido com sucesso.");
        } else {
            System.out.println("Opção inválida.");
        }
    }

    /**
     * Mostra o inventário, lê a escolha e usa o item.
     * Serve para combate (inimigo != null) e para fora dele (inimigo == null).
     * Retorna true se o jogador realmente usou um item (gastou a ação).
     */
    public boolean menuUsarItem(Scanner sc, Personagem personagem, Inimigo inimigo) {

        if (!temItens()) {
            System.out.println("Você não tem itens!");
            return false;
        }

        mostrarInventario();
        System.out.println("Digite o número do item (0 para voltar): ");

        int escolha;
        try {
            escolha = Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Opção inválida.");
            return false;
        }

        if (escolha == 0) {
            return false;
        }

        return usarItem(escolha, personagem, inimigo);
    }


    public boolean usarItem(int escolha, Personagem personagem, Inimigo inimigo) {

        if (escolha < 1 || escolha > itens.size()) {
            System.out.println("Opção inválida.");
            return false;
        }

        Item item = itens.get(escolha - 1);
        boolean emCombate = (inimigo != null);

        if (item.tipo.equalsIgnoreCase("cura")) {
            if (personagem.pHp >= personagem.pMaxHp) {
                System.out.println("Sua vida já está cheia!");
                return false;
            }

            personagem.pHp = Math.min(personagem.pHp + item.valor, personagem.pMaxHp);

            System.out.println("Você usou " + item.nome + "!");
            System.out.println("Você recuperou " + item.valor + " de vida.");
            System.out.println("Vida atual: " + personagem.pHp + "/" + personagem.pMaxHp);

            itens.remove(escolha - 1);
            return true;
        }

        // Isso é para o combate não mexam
        if (!emCombate) {
            System.out.println(item.nome + " só pode ser usado em combate.");
            return false;
        }

        if (item.tipo.equalsIgnoreCase("aumento_dano")) {
            if (personagem.duracaoBonusAttk > 0) {
                System.out.println("O efeito de ataque já está ativo!");
                return false;
            }

            personagem.pAttk += item.valor;
            personagem.bonusAttk = item.valor;
            personagem.duracaoBonusAttk = DURACAO_BUFF;

            System.out.println("Você usou " + item.nome + "!");
            System.out.println("Seu Ataque aumentou em " + item.valor + " por 3 turnos.");

            itens.remove(escolha - 1);
            return true;

        } else if (item.tipo.equalsIgnoreCase("aumento_critico")) {
            if (personagem.duracaoBonusCrit > 0) {
                System.out.println("O efeito de crítico já está ativo!");
                return false;
            }

            personagem.pCrit += item.valor;
            personagem.bonusCrit = item.valor;
            personagem.duracaoBonusCrit = DURACAO_BUFF;

            System.out.println("Você usou " + item.nome + "!");
            System.out.println("Seu Crítico aumentou em " + item.valor + " por 3 turnos.");

            itens.remove(escolha - 1);
            return true;

        } else if (item.tipo.equalsIgnoreCase("dano")) {
            inimigo.hp -= item.valor;

            System.out.println("Você usou " + item.nome + "!");
            System.out.println("Você causou " + item.valor + " de dano no " + inimigo.nome + "!");
            return true;
        }

        System.out.println("Esse item não pode ser usado agora.");
        return false;
    }

    // Chamar no fim de cada turno de combate
    public void atualizarBuffs(Personagem personagem) {

        if (personagem.duracaoBonusCrit > 0) {
            personagem.duracaoBonusCrit--;

            if (personagem.duracaoBonusCrit == 0) {
                System.out.println("O efeito da Poção de Crítico acabou!");
                personagem.pCrit -= personagem.bonusCrit;
                personagem.bonusCrit = 0;
            }
        }

        if (personagem.duracaoBonusAttk > 0) {
            personagem.duracaoBonusAttk--;

            if (personagem.duracaoBonusAttk == 0) {
                System.out.println("O efeito da Poção de Ataque acabou!");
                personagem.pAttk -= personagem.bonusAttk;
                personagem.bonusAttk = 0;
            }
        }
    }

    // Chamar ao sair do combate (vitória, fuga ou derrota) para não deixar buff permanente no personagem por favor
    public void limparBuffs(Personagem personagem) {

        if (personagem.duracaoBonusAttk > 0) {
            personagem.pAttk -= personagem.bonusAttk;
            personagem.bonusAttk = 0;
            personagem.duracaoBonusAttk = 0;
        }

        if (personagem.duracaoBonusCrit > 0) {
            personagem.pCrit -= personagem.bonusCrit;
            personagem.bonusCrit = 0;
            personagem.duracaoBonusCrit = 0;
        }
    }
}