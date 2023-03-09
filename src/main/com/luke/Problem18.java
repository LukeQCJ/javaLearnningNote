package com.luke;

import java.util.Arrays;
import java.util.Scanner;

public class Problem18 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        String heightString = sc.nextLine();
        String weightString = sc.nextLine();
        String[] he = heightString.split(" ");
        String[] kg = weightString.split(" ");
        int[] h = new int[n];
        int[] k = new int[n];
        for(int i=0;i<n;i++){
            h[i]=Integer.parseInt(he[i]);
            k[i]=Integer.parseInt(kg[i]);
        }
        st[] sts = new st[n];
        for(int i=0;i<n;i++){
            sts[i]=new st(i+1,h[i],k[i]);
        }
        Arrays.sort(sts, (o1, o2) -> o1.h==o2.h ? o1.k - o2.k : o1.h - o2.h);
        for (st s : sts) {
            System.out.print(s.n+" ");
        }
    }

    static class st{
        int n;
        int h;
        int k;
        st(int n, int h, int k  ){
            this.n=n;
            this.h=h;
            this.k=k;
        }
    }
}
