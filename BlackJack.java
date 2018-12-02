/* 
 * [게임 규칙]
 * 
 * Common
 * 1. 유저와 딜러와의 대결
 * 2. 52장의 카드를 사용 4개의 문양 각 13장씩 But 문양끼리 의미는 없으며 숫자로 결과판단.
 * 3. 유저와 딜러에게 basic Card 2장씩 배분
 * 4. J, Q, K는 10점
 * 5. A는 원래는 1점 or 11점으로 선택할 수 있지만,
 *    점수 > 21점 일때 1점으로 계산, 점수 <= 21점 일때 11점으로 계산
 * 
 * User
 * 1. 결과가 나올 때 까지 'Hit'or'Stay'를 결정해야 함
 * 2. 'Hit'결정 시 유저는 카드 1장을 뽑는다. 
 * 3. 'Hit'는 4번까지 가능. 총 6개의 카드를 가질 수 있다.
 * 4. 'Stay'결정 시 카드 모두 공개 후, 점수를 비교 >> 승패 결정
 * 
 * Dealer
 * 1. 딜러의 첫 번째 카드는 유저에게 보여주지 않고, ?로 표시
 * 2. 유저가 'Hit'or'Stay'를 결정하면, 점수를 비교함
 *    만약 딜러의 점수가 17이하일 경우 1장을 뽑음
 *  
 * 승패
 * 1.유저 승리
 *   유저의 basic Card가 (A,K)블랙잭 > 딜러의 점수
 *   유저&딜러 <=21, 유저 > 딜러
 *   유저 <= 21, 딜러 > 21
 *  
 * 2.유저 패배
 *   딜러의 basic Card가 (A,K)블랙잭 > 유저의 점수
 *   유저&딜러 <=21, 딜러 > 유저
 *   딜러 <= 21, 유저 > 21
 *  
 * 3.무승부
 *   유저 점수 = 딜러 점수
 *   둘다 블랙잭일 경우
 */
 
//--------------------------method시작------------------//
import java.util.Scanner;
class BlackJack {
    public int main(int betAmount) {
        Scanner scan = new Scanner(System.in);
        System.out.println("카지노에 오신걸 환영합니다!!!");
        System.out.println();
        
        //무한루프
        /*
        for(int s=0;s<1;s=s) {

            
            Scanner keyboard=new Scanner(System.in);
           int Play_Out;
           System.out.println("블랙잭을 즐기고 싶으시면 '1' 카지노에서 나가실꺼면 '2'를 눌러주세요");
           Play_Out=keyboard.nextInt();
           keyboard.nextLine();
           if(Play_Out==2) {
                System.out.println("안녕히 가세여~.");
              System.exit(0);
           }
           else {*/
            System.out.println("지금부터 블랙잭을 시작하겠습니다.");
      
        boolean UserCardinitial = false;        // 유저의 카드 초기화
        boolean DealerCardinitial = false;        // 딜러의 카드 초기화
        boolean victory = true;              // 게임 결과, 승리로 초기화함
        int commonCard[] = new int[52];      // 랜덤으로 결정된 숫자 저장
 
        commonCardInitial(commonCard);          // 공용카드 생성 및 초기화
        

        //기성: 여기서부터 서버가 하는일
        
        // 플레이어, 딜러 블랙잭 판단하는 공간
        boolean UserBJ[] = new boolean[2];        // 유저가 블랙잭인지 판단
        boolean DealerBJ[] = new boolean[2];        // 딜러가 블랙잭인지 판단
        boolean UserBJVic = false;                // 유저의 블랙잭 승리 상태 초기화
        boolean DealerBJVic = false;                // 딜러의 블랙잭 승리 상태 초기화
        
        // 카드 기본 셋팅: 유저 2장, 딜러 2장 배분
        String UserCard[] = new String[6];            // 유저의 카드 준비
        String DealerCard[] = new String[3];            // 딜러의 카드 준비
        cardSetInit(UserCard, DealerCard);              // 유저과 딜러의 카드 생성
        basicCardSetInit(commonCard, UserCard, UserBJ);        // 유저의 기본 카드 2장 배분
        basicCardSetInit(commonCard, DealerCard, DealerBJ);        // 딜러의 기본 카드 2장 배분
         
        // 게임 진행
        BlackJackLoop(commonCard, UserCard, DealerCard, UserCardinitial, DealerCardinitial,
                UserBJ, DealerBJ, UserBJVic, DealerBJVic, victory, scan);
 
        // 결과 출력
        int resultMoney;
        resultMoney = BlackJackResult(UserCard, DealerCard, UserBJVic, DealerBJVic, victory, betAmount);
        
        System.out.println("안녕히 가세여~.");
        
        return resultMoney;
           }
    /*
        }
        scan.close();
           
    }
    */
 
//--------------------------method---------------------------//
 
/*
 * 52장의 공용카드셋 초기화
 * 동일카드는 중복X(4개의 문양 13장씩 총52장)
 * 랜덤으로 섞음
 */
    public void commonCardInitial(int commonCard[]) {
        
        boolean noSame[] = new boolean[52];
        
        // 무작위로 섞어 중복불가에 대한 준비
        for(int i = 0; i < noSame.length ; i++) {
            noSame[i] = false;
        }
        
        // 중복없는 숫자가 나올 때까지 무작위 결정을 반복
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
    
    // 섞인 공용 카드set을 출력
    public void comCardSetPrint(int cardSet[]) {
        for(int i=0; i < cardSet.length; i++) {
            if (i % 3 == 0) {
                System.out.println();
            }
            System.out.print("cardSet[" + i + "] = " + cardSet[i] + " ");
        }
        System.out.println("\n");
    }
    
    // 유저과 딜러의 카드set 초기화
    public void cardSetInit(String UserCard[], String DealerCard[]) {
        for (int i = 0; i < UserCard.length; i++) {
            UserCard[i] = "0";
        }
        for (int i = 0; i < DealerCard.length; i++) {
            DealerCard[i] = "0";
        }
    }
    
    // 공용 카드에서 초기 카드 2장 뽑기
    public void basicCardSetInit(int commonCard[], String cardSet[], boolean blackjack[]) {
        String drawCardStr = "";
        int drawCard = 0;
        int cardSetPos = 0;
        int drawLoop = 0;
        
        // 공용 카드set에서 뽑은 카드는 다시 뽑지 않음
        while (drawLoop < 2) {
            cardSetPos = (int)(Math.random() * 52);
            drawCard = commonCard[cardSetPos];
            if (drawCard != 0) {
                // 테스트: 공용 카드set에서 뽑은 카드의 위치 확인
                // System.out.println("뽑은 카드의 배열 인덱스 = " + cardSetPos);
                
                drawCardStr = checkNum(drawCard, blackjack);
                cardSet[drawLoop] = drawCardStr;
                commonCard[cardSetPos] = 0;
                drawLoop++;
            }
        }
 

    }
 
/*
 * 카드번호 체크 메소드는 두 가지 종류가 있으며,
 * 첫 번째 체크시에만 블랙잭을 판단
 */
    // 카드번호 체크 및 변환 + 블랙잭 판단
    public String checkNum(int cardNum, boolean blackjack[]) {
        String result = "";
 
        // 뽑은 카드의 번호가 '1'이면 'A'로 변환 
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
 
        // 카드 번호가 '11' 이상이면 'J, Q, K'로 변환
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
        // 일반 숫자면 변환 없음
        else {
            if (cardNum > 1 && cardNum <= 10) {
                result = Integer.toString(cardNum);
            }
        }
 
        return result;
    }
    
    // 카드번호 체크 및 변환
    public String checkNum(int cardNum) {
        String result = "";
 
        // 실제 카드 숫자로 변환
        if (cardNum == 1 || cardNum % 13 == 1) {
            result = "A";
        } else if (cardNum % 13 == 0) {
            cardNum = 13;
        }
        else {
            cardNum = (cardNum % 13);
        }
 
        // 카드 번호가 '11' 이상이면 'J, Q, K'로 변환
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
        // 일반 숫자면 변환 없음
        else {
            result = Integer.toString(cardNum);
        }
 
        return result;
    }
    
    // 게임 반복 루프
    public void BlackJackLoop(int commonCard[], String UserCard[], String DealerCard[],
            boolean UserCardinitial, boolean DealerCardinitial,
            boolean UserBJ[], boolean DealerBJ[],
            boolean UserBJVic, boolean DealerBJVic, boolean victory, Scanner scan) {
        
      
        // 최초에 배분된 기본 카드 2장을 각각 공개
        System.out.println("유저카드▽");
        cardSetNum(UserCard, 1);
        
        System.out.println("딜러카드▼");
        cardSetNum(DealerCard, 2);
        System.out.println();
        
        // 유저의 기본 카드 2장이 'A'와 'K'면 유저의 블랙잭 승리로 판단
        if (UserBJ[0] == true && UserBJ[1] == true) {
            UserBJVic = true;
        } 
        // 딜러의 기본 카드 2장이 'A'와 'K'면 딜러의 블랙잭 승리로 판단
        if (DealerBJ[0] == true && DealerBJ[1] == true) {
            DealerBJVic = true;
        }
        
        // 반복 시작
        int game=0;
        //기성: 여기서부터가 중요 아직까진 다 서버 역할
        while (game==0) {
            // 카드를 뽑은 후 유저의 점수가 '21'을 초과하면 즉시 게임 패배
            if (pointResult(UserCard) > 21) {
                victory = false;
                game++;
            }
            
            // 유저 or 딜러가 '블랙잭'이면 즉시 게임 종료
            if (UserBJVic || DealerBJVic) {
               game++;
            } else if (!victory) {
               game++;
            }
            else {
            // 유저에게 'Hit or Stay' 물어봄
            // 기성: 여기서부터 이제 클라이언트 역할.
            System.out.print("카드를 더 받으시려면 Hit(1)\n"+"멈추고 결과를 보시려면 Stay(2) \n");
            System.out.print("Hit하시겠습니까? or Stay하시겠습니까? : ");
            int decision = scan.nextInt();
            scan.nextLine();
            System.out.println();
            
            //기성: 클라이언트 역할 끝 다시 서버로 
        
                // 유저이 'Hit' 결정 시 추가 카드 1장 받음            
                if (decision == 1) {
        
                    // 카드 뽑기 조건: 대상의 카드set이 비어있는 상태이어야 함
                    if (!cardFull(UserCard, UserCardinitial)) {
                        drawCardOne(commonCard, UserCard);        // 카드 뽑기
                    }
                    // 카드가 꽉 찾을 경우 강제로 'Stay'로 판단
                    else {
                        System.out.println("더 이상 카드를 받을 수 없습니다.");
                        decision = 2;
                    }
          
                 
                    // 카드를 뽑은 후 유저의 점수가 '21'을 초과하면 즉시 게임 패배
                    if (pointResult(UserCard) > 21) {
                        victory = false;
                        game++;
                    }
                    
                    System.out.println("유저카드▽");
                    cardSetNum(UserCard, 1);
                    
                    System.out.println("딜러카드▼");
                    cardSetNum(DealerCard, 2);
                    
                } else if (decision == 2) {                    
                    // 카드 뽑기 조건: 딜러 < 17, 카드가 2개인 상태
                   for(int h=0;h<1;h=h) {
                    if (pointResult(DealerCard) < 17) {        
                        drawCardOne(commonCard, DealerCard);        // 카드 뽑기
                    }
                    else {
                       
                       game++;
                       break;
                    }
                   }
                }
                else {
                    // 예외 처리
                    System.out.println("??? : 다시 말씀해주시죠..");
                    System.out.println();
                }
            }
        }
        
        //루프
    }
    
    
    
    
    // 카드 1장 뽑기
    public void drawCardOne(int commonCard[], String cardSet[]) {
        int drawLoop = 0;
        int cardSetPos = 0;
        int drawCard = 0;
        String drawCardStr = "";
        
        while (drawLoop < 1) {
            cardSetPos = (int)(Math.random() * 52);
            drawCard = commonCard[cardSetPos];
    
            if (drawCard != 0) {
                // 테스트: 공용 카드set에서 뽑은 카드의 위치 확인
                // System.out.println("뽑은 카드의 배열 인덱스 = " + cardSetPos);
    
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
 
    // 배분된 카드 출력
    public void cardSetNum(String cardSet[], int index) {
        
        // 'index' 값이 1이면 카드set 공개
        // 'index' 값이 2면 카드set에서 카드 1개 숨김
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
                if (i == 0) {        // 첫 번째 자리의 카드 숨김
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
            // 예외 처리
            System.out.println("에러. 잘못입력했습니다");
        }
    }
    
    // 유저의 카드set이 꽉 찬 상태인지 체크
    public boolean cardFull(String cardSet[], boolean cardSetFull) {
        int count = 0;
        for (int i = 0; i < cardSet.length; i++) {
            if (cardSet[i] == "0") {
                count++;
            }
        }
        // 비어있는 개수가 없으면 꽉 찬 상태
        if (count == 0) {
            cardSetFull = true;
        }
        
        return cardSetFull;
    }
    
    // 점수 구하기
    public int pointResult(String cardSet[]) {
        int point = 0;
        
        // 1차 점수 합산: 알파벳 카드는 따라 점수를 다르게 합산
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
        // 2차 점수 합산: 1차 점수 합산이 끝난 뒤 카드set에 'A'가 존재할 경우,
        // 10점을 추가한 점수가 '21점 이하'면 10점 추가(= 'A'는 '11점'과 같은 의미)
        for (int i = 0; i < cardSet.length; i++) {
            if (cardSet[i] == "A" && point + 10 <= 21) {
                point += 10;
            } 
        }
    
        return point;
    }
    
/*
 * 승패
 * 1.유저 승리
 *  - 유저의 기본 카드 2장이 블랙잭(A, K)이고, 딜러는 해당되지 않을 경우
 *  - 각자 점수가 '21점 이하'이고, 유저의 점수가 더 높을 경우
 *  - 유저의 점수가 '21점 이하'이고, 딜러의 점수는 '21점을 초과'할 경우
 *  
 * 2.유저 패배
 *  - 딜러의 기본 카드 2장이 블랙잭(A, K)이고, 유저는 해당되지 않을 경우
 *  - 각자 점수가 '21점 이하'이고, 딜러의 점수가 더 높을 경우
 *  - 유저의 점수가 '21점을 초과'할 경우
 *  
 * 3.무승부
 *  - 각자 기본 카드 2장이 블랙잭(A, K)일 경우 
 *  - 각자 점수가 동일한 경우 
 */
    
    // 결과 출력
    public int BlackJackResult(String UserCard[], String DealerCard[], 
            boolean UserBJVic, boolean DealerBJVic, boolean victory, int betAmount) {
        System.out.println("카드를 오픈할게요!!!.");
        System.out.println();
        
        // 유저에게 배분된 카드와 점수 출력
        System.out.println("유저의 카드입니다.");
        cardSetNum(UserCard, 1);
        
        System.out.println("유저의 점수 = " + pointResult(UserCard));
        System.out.println();
        
        // 딜러에게 배분된 카드와 점수 출력
        System.out.println("딜러의 카드입니다.");
        cardSetNum(DealerCard, 1);
        
        System.out.println("딜러의 점수 = " + pointResult(DealerCard));
        System.out.println();
        
        // 결과 메시지 - 블랙잭 유무
        if (UserBJVic && !DealerBJVic) {
            System.out.println("키야~ 기가막히게 유저분께서 '블랙잭'으로 이겼습니다!");
            return betAmount * 2;
        } else if (!UserBJVic && DealerBJVic) {
            System.out.println("ㅋ '블랙잭'으로 패배하셨습니다. 안타깝게도 빚더미가 증가하였습니다.");
            return betAmount * -1;
        } else if (UserBJVic && DealerBJVic) {
            System.out.println("이럴수가!! '블랙잭'으로 무승부!!!");
            return 0;
        }
        // 결과 메시지 - 점수 비교
        else {
            if (victory && pointResult(UserCard) > pointResult(DealerCard) 
                    && pointResult(UserCard) <= 21
                    || victory && pointResult(UserCard) < pointResult(DealerCard)
                    && pointResult(DealerCard) > 21) {
                System.out.println("축하드립니다! 유저분의 승리입니다!");
                return betAmount;
            } else if (victory && pointResult(UserCard) == pointResult(DealerCard)
                    && pointResult(UserCard) <= 21) {
                System.out.println("이럴수가... 무승부입니다!");
                return 0;
            }
            else {
                System.out.println("ㅋ 안타깝게도 빚더미가 증가하였습니다.");
                return (betAmount * -1);
            }
        }
    }
}