package TextualGame;

public class Personagem {
    public String pName = "";
    public int pHp = 100, pMaxHp = 100, pAttk = 15, pCrit = 2, pLuck = 5;

    //Variáveis para armazenar o valor do bonus de aumento das porções e para ir reduzindo o tempo delas por turno de luta, para que o buff das porções não seja permanente
    public int bonusAttk = 0, duracaoBonusAttk = 0, bonusCrit = 0, duracaoBonusCrit = 0, bonusLuck = 0, duracaoBonusLuck = 0;

}
