import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        int[] money = {50000,	10000,	5000,	1000,	500,	100};
        int[] charges = {	1,		1,		1,		1,		5,	10};

        int tickets = 5; // 식권 갯수
        int price = 3200; // 식권 가격

        String fileName = "kiosk.txt";	// 잔돈 및 티켓 수 저장 및 로드
        File file = new File(fileName);
        FileWriter fw = null;
        FileReader fr = null;
        BufferedReader br = null;

        if(file.exists()) {
            try {
                fr = new FileReader(fileName);
                br = new BufferedReader(fr);

                String str = br.readLine();
                String[] split = str.split("/");
                tickets = Integer.parseInt(split[1]);
                String[] chargeStr = split[0].split(",");
                for(int i=0;i<charges.length;i++) {
                    charges[i] = Integer.parseInt(chargeStr[i]);
                }

                br.close();
                fr.close();

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        while (true) {
            System.out.println("[식권 자판기]");
            for(int i=0;i<money.length;i++) {
                System.out.print(money[i] + "원 ["+charges[i]+"] ");
            }
            System.out.println();

            String ticStr = tickets==0 ? "매진" : Integer.toString(tickets) + "매";
            System.out.println("식권 : " + ticStr);

            System.out.print("[1]관리자 [2]사용자 >>> ");
            int menu = scan.nextInt();
            scan.nextLine();

            if(menu==1) {	// 관리자
                while (true) {
                    System.out.println("[관리자]");
                    System.out.print("[1]식권충전 [2]잔돈충전 [3]뒤로가기 >>> ");
                    int sel = scan.nextInt();
                    scan.nextLine();
                    if(sel==1) {	// 식권 충전
                        System.out.print("충전할 식권 갯수 : ");
                        int num = scan.nextInt();
                        tickets+=num;
                    }else if (sel==2) {	// 잔돈충전
                        for(int i=0;i<money.length;i++) {
                            System.out.print("입금할 "+money[i]+"원 매수 : ");
                            int num = scan.nextInt();
                            if(num>=0) {
                                charges[i] += num;
                            }
                        }
                    }else if (sel==3) {	// 뒤로가기
                        break;
                    }
                }

            }
            else if (menu==2) {	// 사용자
                while (true) {
                    System.out.println("[사용자]");
                    System.out.print("[1]식권구매 [2]뒤로가기 >>> ");
                    int sel = scan.nextInt();
                    if(sel==1) {	// 구입
                        int total = 0;
                        int[] inputs = new int[money.length];
                        for(int i=0;i<money.length;i++) {
                            System.out.print("입금할 "+money[i]+"원 매수 : ");
                            int num = scan.nextInt();
                            if(num>=0) {
                                inputs[i] = num;
                                total += num*money[i];
                            }
                        }

                        if(total<=0) {
                            System.out.println("돈을 넣어주세요");
                        }
                        else {
                            System.out.print("구매 매수 : ");
                            int input = scan.nextInt();
                            if(input<=0) {
                                System.out.println("양수만 입력 가능");
                            }
                            else if (input>tickets) {
                                System.out.println("티켓 부족");
                            }
                            else {
                                if(total>=input*price) {
                                    int remains = total-(input*price);
                                    int[] returnMoney = new int[money.length];
                                    for(int i=0;i<inputs.length;i++) {
                                        inputs[i] += charges[i];
                                    }

                                    for(int i=0;i<money.length;i++) {
                                        if(remains>=money[i] && inputs[i]>0) {
                                            remains-=money[i];
                                            inputs[i]--;
                                            returnMoney[i]++;
                                            i--;
                                        }
                                    }

                                    if(remains>0) {
                                        System.out.println("거스름돈 부족");
                                    }
                                    else {
                                        charges = inputs;
                                        tickets-=input;
                                        System.out.print("반환 :  ");
                                        for(int i=0;i<returnMoney.length;i++) {
                                            System.out.print(money[i] + "원 ["+returnMoney[i]+"] ");
                                        }
                                        System.out.println();
                                    }

                                }else {
                                    System.out.println("금액 부족");
                                }
                            }

                        }

                    }
                    else if (sel==2) {	// 뒤로가기
                        break;
                    }
                }
            }

            try {
                fw = new FileWriter(fileName);

                String data = "";

                for(int i=0;i<charges.length;i++) {
                    data+=Integer.toString(charges[i]);
                    if(i<charges.length-1) {
                        data+=",";
                    }
                }
                data+="/";
                data+=Integer.toString(tickets);

                fw.write(data);
                fw.close();

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }
}