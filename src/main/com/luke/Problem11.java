package com.luke;

import java.util.*;

public class Problem11 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        // a1-a2,a5-a6,a2-a3
        String inputServiceLinkedListString = sc.nextLine();
        String[] serviceRelations = inputServiceLinkedListString.split(",");
        // a5,a2
        String inputBrokenServiceLinkedListString = sc.nextLine();
        String[] brokenServices = inputBrokenServiceLinkedListString.split(",");
        String availableService = getAvailableService(serviceRelations, brokenServices);
        if ("".equals(availableService)) {
            System.out.println(",");
        } else {
            availableService = availableService.substring(0, availableService.lastIndexOf(","));
            System.out.println(availableService);
        }
    }

    public static String getAvailableService(String[] serviceRelations,String[] brokenServices) {
        Set<String> brokenServicesSet = new HashSet<>(Arrays.asList(brokenServices));
        StringBuilder availableService = new StringBuilder();
        for (String serviceRelationStr : serviceRelations) {
            String[] services = serviceRelationStr.split("-");
            Queue<String> availableServiceQueue = new LinkedList<>();
            for (String service : services) {
                if (brokenServicesSet.contains(service)) {
                    availableServiceQueue.clear();
                } else {
                    availableServiceQueue.add(service);
                }
            }
            while (!availableServiceQueue.isEmpty()) {
                availableService.append(availableServiceQueue.poll()).append(",");
            }
        }
        return availableService.toString();
    }
}
