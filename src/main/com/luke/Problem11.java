package com.luke;

import java.util.Scanner;

public class Problem11 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        // a1-a2,a5-a6,a2-a3
        String inputServiceLinkedListString = sc.nextLine();
        String[] serviceRelationStrArray = inputServiceLinkedListString.split(",");
        // a5,a2
        String inputBrokenServiceLinkedListString = sc.nextLine();
        String[] brokenServiceStrArray = inputBrokenServiceLinkedListString.split(",");
    }
}
