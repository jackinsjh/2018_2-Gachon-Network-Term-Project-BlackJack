/* 
 * [���� ��Ģ]
 * 
 * Common
 * 1. ������ �������� ���
 * 2. 52���� ī�带 ��� 4���� ���� �� 13�徿 But ���糢�� �ǹ̴� ������ ���ڷ� ����Ǵ�.
 * 3. ������ �������� basic Card 2�徿 ���
 * 4. J, Q, K�� 10��
 * 5. A�� ������ 1�� or 11������ ������ �� ������,
 *    ���� > 21�� �϶� 1������ ���, ���� <= 21�� �϶� 11������ ���
 * 
 * User
 * 1. ����� ���� �� ���� 'Hit'or'Stay'�� �����ؾ� ��
 * 2. 'Hit'���� �� ������ ī�� 1���� �̴´�. 
 * 3. 'Hit'�� 4������ ����. �� 6���� ī�带 ���� �� �ִ�.
 * 4. 'Stay'���� �� ī�� ��� ���� ��, ������ �� >> ���� ����
 * 
 * Dealer
 * 1. ������ ù ��° ī��� �������� �������� �ʰ�, ?�� ǥ��
 * 2. ������ 'Hit'or'Stay'�� �����ϸ�, ������ ����
 *    ���� ������ ������ 17������ ��� 1���� ����
 *  
 * ����
 * 1.���� �¸�
 *   ������ basic Card�� (A,K)���� > ������ ����
 *   ����&���� <=21, ���� > ����
 *   ���� <= 21, ���� > 21
 *  
 * 2.���� �й�
 *   ������ basic Card�� (A,K)���� > ������ ����
 *   ����&���� <=21, ���� > ����
 *   ���� <= 21, ���� > 21
 *  
 * 3.���º�
 *   ���� ���� = ���� ����
 *   �Ѵ� ������ ���
 */
 
//--------------------------method����------------------//
import java.util.Scanner;
class BlackJack {
    public int main(int betAmount) {
        Scanner scan = new Scanner(System.in);
        System.out.println("ī���뿡 ���Ű� ȯ���մϴ�!!!");
        System.out.println();
        
        //���ѷ���
        /*
        for(int s=0;s<1;s=s) {

            
            Scanner keyboard=new Scanner(System.in);
           int Play_Out;
           System.out.println("������ ���� �����ø� '1' ī���뿡�� �����ǲ��� '2'�� �����ּ���");
           Play_Out=keyboard.nextInt();
           keyboard.nextLine();
           if(Play_Out==2) {
                System.out.println("�ȳ��� ������~.");
              System.exit(0);
           }
           else {*/
            System.out.println("���ݺ��� ������ �����ϰڽ��ϴ�.");
      
        boolean UserCardinitial = false;        // ������ ī�� �ʱ�ȭ
        boolean DealerCardinitial = false;        // ������ ī�� �ʱ�ȭ
        boolean victory = true;              // ���� ���, �¸��� �ʱ�ȭ��
        int commonCard[] = new int[52];      // �������� ������ ���� ����
 
        commonCardInitial(commonCard);          // ����ī�� ���� �� �ʱ�ȭ
        

        //�⼺: ���⼭���� ������ �ϴ���
        
        // �÷��̾�, ���� ���� �Ǵ��ϴ� ����
        boolean UserBJ[] = new boolean[2];        // ������ �������� �Ǵ�
        boolean DealerBJ[] = new boolean[2];        // ������ �������� �Ǵ�
        boolean UserBJVic = false;                // ������ ���� �¸� ���� �ʱ�ȭ
        boolean DealerBJVic = false;                // ������ ���� �¸� ���� �ʱ�ȭ
        
        // ī�� �⺻ ����: ���� 2��, ���� 2�� ���
        String UserCard[] = new String[6];            // ������ ī�� �غ�
        String DealerCard[] = new String[3];            // ������ ī�� �غ�
        cardSetInit(UserCard, DealerCard);              // ������ ������ ī�� ����
        basicCardSetInit(commonCard, UserCard, UserBJ);        // ������ �⺻ ī�� 2�� ���
        basicCardSetInit(commonCard, DealerCard, DealerBJ);        // ������ �⺻ ī�� 2�� ���
         
        // ���� ����
        BlackJackLoop(commonCard, UserCard, DealerCard, UserCardinitial, DealerCardinitial,
                UserBJ, DealerBJ, UserBJVic, DealerBJVic, victory, scan);
 
        // ��� ���
        int resultMoney;
        resultMoney = BlackJackResult(UserCard, DealerCard, UserBJVic, DealerBJVic, victory, betAmount);
        
        System.out.println("�ȳ��� ������~.");
        
        return resultMoney;
           }
    /*
        }
        scan.close();
           
    }
    */
 
//--------------------------method---------------------------//
 
/*
 * 52���� ����ī��� �ʱ�ȭ
 * ����ī��� �ߺ�X(4���� ���� 13�徿 ��52��)
 * �������� ����
 */
    public void commonCardInitial(int commonCard[]) {
        
        boolean noSame[] = new boolean[52];
        
        // �������� ���� �ߺ��Ұ��� ���� �غ�
        for(int i = 0; i < noSame.length ; i++) {
            noSame[i] = false;
        }
        
        // �ߺ����� ���ڰ� ���� ������ ������ ������ �ݺ�
        int randLoop = 0;
        int randTemp;
        while(randLoop < 52) {
            randTemp = (int)(Math.random() * 52);
            if(noSame[randTemp] == false) {
                noSame[randTemp] = true;
                commonCard[randLoop] = randTemp + 1;
                randLoop++;
            }
        }
    }
    
    // ���� ���� ī��set�� ���
    public void comCardSetPrint(int cardSet[]) {
        for(int i=0; i < cardSet.length; i++) {
            if (i % 3 == 0) {
                System.out.println();
            }
            System.out.print("cardSet[" + i + "] = " + cardSet[i] + " ");
        }
        System.out.println("\n");
    }
    
    // ������ ������ ī��set �ʱ�ȭ
    public void cardSetInit(String UserCard[], String DealerCard[]) {
        for (int i = 0; i < UserCard.length; i++) {
            UserCard[i] = "0";
        }
        for (int i = 0; i < DealerCard.length; i++) {
            DealerCard[i] = "0";
        }
    }
    
    // ���� ī�忡�� �ʱ� ī�� 2�� �̱�
    public void basicCardSetInit(int commonCard[], String cardSet[], boolean blackjack[]) {
        String drawCardStr = "";
        int drawCard = 0;
        int cardSetPos = 0;
        int drawLoop = 0;
        
        // ���� ī��set���� ���� ī��� �ٽ� ���� ����
        while (drawLoop < 2) {
            cardSetPos = (int)(Math.random() * 52);
            drawCard = commonCard[cardSetPos];
            if (drawCard != 0) {
                // �׽�Ʈ: ���� ī��set���� ���� ī���� ��ġ Ȯ��
                // System.out.println("���� ī���� �迭 �ε��� = " + cardSetPos);
                
                drawCardStr = checkNum(drawCard, blackjack);
                cardSet[drawLoop] = drawCardStr;
                commonCard[cardSetPos] = 0;
                drawLoop++;
            }
        }
 

    }
 
/*
 * ī���ȣ üũ �޼ҵ�� �� ���� ������ ������,
 * ù ��° üũ�ÿ��� ������ �Ǵ�
 */
    // ī���ȣ üũ �� ��ȯ + ���� �Ǵ�
    public String checkNum(int cardNum, boolean blackjack[]) {
        String result = "";
 
        // ���� ī���� ��ȣ�� '1'�̸� 'A'�� ��ȯ 
        if (cardNum == 1 || cardNum % 13 == 1) {
            result = "A";
            blackjack[0] = true;
        } else if (cardNum % 13 == 0) {
            cardNum = 13;
            blackjack[1] = true;
        }
        else {
            cardNum = (cardNum % 13);
        }
 
        // ī�� ��ȣ�� '11' �̻��̸� 'J, Q, K'�� ��ȯ
        if (cardNum > 10) {
            switch (cardNum) {
                case 11:
                    result = "J";
                    break;
                case 12:
                    result = "Q";
                    break;
                case 13:
                    result = "K";
                    break;
            }
        }
        // �Ϲ� ���ڸ� ��ȯ ����
        else {
            if (cardNum > 1 && cardNum <= 10) {
                result = Integer.toString(cardNum);
            }
        }
 
        return result;
    }
    
    // ī���ȣ üũ �� ��ȯ
    public String checkNum(int cardNum) {
        String result = "";
 
        // ���� ī�� ���ڷ� ��ȯ
        if (cardNum == 1 || cardNum % 13 == 1) {
            result = "A";
        } else if (cardNum % 13 == 0) {
            cardNum = 13;
        }
        else {
            cardNum = (cardNum % 13);
        }
 
        // ī�� ��ȣ�� '11' �̻��̸� 'J, Q, K'�� ��ȯ
        if (cardNum > 10) {
            switch (cardNum) {
                case 11:
                    result = "J";
                    break;
                case 12:
                    result = "Q";
                    break;
                case 13:
                    result = "K";
                    break;
            }
        }
        // �Ϲ� ���ڸ� ��ȯ ����
        else {
            result = Integer.toString(cardNum);
        }
 
        return result;
    }
    
    // ���� �ݺ� ����
    public void BlackJackLoop(int commonCard[], String UserCard[], String DealerCard[],
            boolean UserCardinitial, boolean DealerCardinitial,
            boolean UserBJ[], boolean DealerBJ[],
            boolean UserBJVic, boolean DealerBJVic, boolean victory, Scanner scan) {
        
      
        // ���ʿ� ��е� �⺻ ī�� 2���� ���� ����
        System.out.println("����ī���");
        cardSetNum(UserCard, 1);
        
        System.out.println("����ī���");
        cardSetNum(DealerCard, 2);
        System.out.println();
        
        // ������ �⺻ ī�� 2���� 'A'�� 'K'�� ������ ���� �¸��� �Ǵ�
        if (UserBJ[0] == true && UserBJ[1] == true) {
            UserBJVic = true;
        } 
        // ������ �⺻ ī�� 2���� 'A'�� 'K'�� ������ ���� �¸��� �Ǵ�
        if (DealerBJ[0] == true && DealerBJ[1] == true) {
            DealerBJVic = true;
        }
        
        // �ݺ� ����
        int game=0;
        //�⼺: ���⼭���Ͱ� �߿� �������� �� ���� ����
        while (game==0) {
            // ī�带 ���� �� ������ ������ '21'�� �ʰ��ϸ� ��� ���� �й�
            if (pointResult(UserCard) > 21) {
                victory = false;
                game++;
            }
            
            // ���� or ������ '����'�̸� ��� ���� ����
            if (UserBJVic || DealerBJVic) {
               game++;
            } else if (!victory) {
               game++;
            }
            else {
            // �������� 'Hit or Stay' ���
            // �⼺: ���⼭���� ���� Ŭ���̾�Ʈ ����.
            System.out.print("ī�带 �� �����÷��� Hit(1)\n"+"���߰� ����� ���÷��� Stay(2) \n");
            System.out.print("Hit�Ͻðڽ��ϱ�? or Stay�Ͻðڽ��ϱ�? : ");
            int decision = scan.nextInt();
            scan.nextLine();
            System.out.println();
            
            //�⼺: Ŭ���̾�Ʈ ���� �� �ٽ� ������ 
        
                // ������ 'Hit' ���� �� �߰� ī�� 1�� ����            
                if (decision == 1) {
        
                    // ī�� �̱� ����: ����� ī��set�� ����ִ� �����̾�� ��
                    if (!cardFull(UserCard, UserCardinitial)) {
                        drawCardOne(commonCard, UserCard);        // ī�� �̱�
                    }
                    // ī�尡 �� ã�� ��� ������ 'Stay'�� �Ǵ�
                    else {
                        System.out.println("�� �̻� ī�带 ���� �� �����ϴ�.");
                        decision = 2;
                    }
          
                 
                    // ī�带 ���� �� ������ ������ '21'�� �ʰ��ϸ� ��� ���� �й�
                    if (pointResult(UserCard) > 21) {
                        victory = false;
                        game++;
                    }
                    
                    System.out.println("����ī���");
                    cardSetNum(UserCard, 1);
                    
                    System.out.println("����ī���");
                    cardSetNum(DealerCard, 2);
                    
                } else if (decision == 2) {                    
                    // ī�� �̱� ����: ���� < 17, ī�尡 2���� ����
                   for(int h=0;h<1;h=h) {
                    if (pointResult(DealerCard) < 17) {        
                        drawCardOne(commonCard, DealerCard);        // ī�� �̱�
                    }
                    else {
                       
                       game++;
                       break;
                    }
                   }
                }
                else {
                    // ���� ó��
                    System.out.println("??? : �ٽ� �������ֽ���..");
                    System.out.println();
                }
            }
        }
        
        //����
    }
    
    
    
    
    // ī�� 1�� �̱�
    public void drawCardOne(int commonCard[], String cardSet[]) {
        int drawLoop = 0;
        int cardSetPos = 0;
        int drawCard = 0;
        String drawCardStr = "";
        
        while (drawLoop < 1) {
            cardSetPos = (int)(Math.random() * 52);
            drawCard = commonCard[cardSetPos];
    
            if (drawCard != 0) {
                // �׽�Ʈ: ���� ī��set���� ���� ī���� ��ġ Ȯ��
                // System.out.println("���� ī���� �迭 �ε��� = " + cardSetPos);
    
                drawCardStr = checkNum(drawCard);
                for (int i = 0; i < cardSet.length; i++) {
                    if (cardSet[i] == "0") {
                        cardSet[i] = drawCardStr;
                        commonCard[cardSetPos] = 0;
                        drawLoop++;
                        break;
                    }
                }
            }
            
        }
    }
 
    // ��е� ī�� ���
    public void cardSetNum(String cardSet[], int index) {
        
        // 'index' ���� 1�̸� ī��set ����
        // 'index' ���� 2�� ī��set���� ī�� 1�� ����
        if (index == 1) {
            for (int i = 0; i < cardSet.length; i++) {
                if (cardSet[i] == "0") {
                    System.out.print(" ");
                }
                else {
                    System.out.print(cardSet[i] + " ");
                }
            }
            System.out.println();
        } else if (index == 2) {
            for (int i = 0; i < cardSet.length; i++) {
                if (i == 0) {        // ù ��° �ڸ��� ī�� ����
                    System.out.print("? ");
                }
                else {
                    if (cardSet[i] == "0") {
                        System.out.print(" ");
                    }
                    else {
                        System.out.print(cardSet[i] + " ");
                    }
                }
            }
            System.out.println();
        } 
        else {
            // ���� ó��
            System.out.println("����. �߸��Է��߽��ϴ�");
        }
    }
    
    // ������ ī��set�� �� �� �������� üũ
    public boolean cardFull(String cardSet[], boolean cardSetFull) {
        int count = 0;
        for (int i = 0; i < cardSet.length; i++) {
            if (cardSet[i] == "0") {
                count++;
            }
        }
        // ����ִ� ������ ������ �� �� ����
        if (count == 0) {
            cardSetFull = true;
        }
        
        return cardSetFull;
    }
    
    // ���� ���ϱ�
    public int pointResult(String cardSet[]) {
        int point = 0;
        
        // 1�� ���� �ջ�: ���ĺ� ī��� ���� ������ �ٸ��� �ջ�
        for (int i = 0; i < cardSet.length; i++) {
            if (cardSet[i] == "A") {
                point += 1;
            } else if (cardSet[i] == "J") {
                point += 10;
            } else if (cardSet[i] == "Q") {
                point += 10;
            } else if (cardSet[i] == "K") {
                point += 10;
            } else if (Integer.parseInt(cardSet[i]) > 1 
                    && Integer.parseInt(cardSet[i]) <= 10) {
                point += Integer.parseInt(cardSet[i]);
            } 
            else {
                point += 0;
            }
        }
        // 2�� ���� �ջ�: 1�� ���� �ջ��� ���� �� ī��set�� 'A'�� ������ ���,
        // 10���� �߰��� ������ '21�� ����'�� 10�� �߰�(= 'A'�� '11��'�� ���� �ǹ�)
        for (int i = 0; i < cardSet.length; i++) {
            if (cardSet[i] == "A" && point + 10 <= 21) {
                point += 10;
            } 
        }
    
        return point;
    }
    
/*
 * ����
 * 1.���� �¸�
 *  - ������ �⺻ ī�� 2���� ����(A, K)�̰�, ������ �ش���� ���� ���
 *  - ���� ������ '21�� ����'�̰�, ������ ������ �� ���� ���
 *  - ������ ������ '21�� ����'�̰�, ������ ������ '21���� �ʰ�'�� ���
 *  
 * 2.���� �й�
 *  - ������ �⺻ ī�� 2���� ����(A, K)�̰�, ������ �ش���� ���� ���
 *  - ���� ������ '21�� ����'�̰�, ������ ������ �� ���� ���
 *  - ������ ������ '21���� �ʰ�'�� ���
 *  
 * 3.���º�
 *  - ���� �⺻ ī�� 2���� ����(A, K)�� ��� 
 *  - ���� ������ ������ ��� 
 */
    
    // ��� ���
    public int BlackJackResult(String UserCard[], String DealerCard[], 
            boolean UserBJVic, boolean DealerBJVic, boolean victory, int betAmount) {
        System.out.println("ī�带 �����ҰԿ�!!!.");
        System.out.println();
        
        // �������� ��е� ī��� ���� ���
        System.out.println("������ ī���Դϴ�.");
        cardSetNum(UserCard, 1);
        
        System.out.println("������ ���� = " + pointResult(UserCard));
        System.out.println();
        
        // �������� ��е� ī��� ���� ���
        System.out.println("������ ī���Դϴ�.");
        cardSetNum(DealerCard, 1);
        
        System.out.println("������ ���� = " + pointResult(DealerCard));
        System.out.println();
        
        // ��� �޽��� - ���� ����
        if (UserBJVic && !DealerBJVic) {
            System.out.println("Ű��~ �Ⱑ������ �����в��� '����'���� �̰���ϴ�!");
            return betAmount * 2;
        } else if (!UserBJVic && DealerBJVic) {
            System.out.println("�� '����'���� �й��ϼ̽��ϴ�. ��Ÿ���Ե� �����̰� �����Ͽ����ϴ�.");
            return betAmount * -1;
        } else if (UserBJVic && DealerBJVic) {
            System.out.println("�̷�����!! '����'���� ���º�!!!");
            return 0;
        }
        // ��� �޽��� - ���� ��
        else {
            if (victory && pointResult(UserCard) > pointResult(DealerCard) 
                    && pointResult(UserCard) <= 21
                    || victory && pointResult(UserCard) < pointResult(DealerCard)
                    && pointResult(DealerCard) > 21) {
                System.out.println("���ϵ帳�ϴ�! �������� �¸��Դϴ�!");
                return betAmount;
            } else if (victory && pointResult(UserCard) == pointResult(DealerCard)
                    && pointResult(UserCard) <= 21) {
                System.out.println("�̷�����... ���º��Դϴ�!");
                return 0;
            }
            else {
                System.out.println("�� ��Ÿ���Ե� �����̰� �����Ͽ����ϴ�.");
                return (betAmount * -1);
            }
        }
    }
}