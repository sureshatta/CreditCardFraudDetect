package com.credit.frauddetect;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class FarudDetect {

	static String[] getSuspiciousList(String[] transactions) {
		List<String> results = new ArrayList<String>();
		List<String[]> objs = new ArrayList<String[]>();
		LinkedHashMap<String, List<List<String>>> personTransMap = new LinkedHashMap<>();
		for (String string : transactions) {
			String[] obj = string.split("\\|");
			objs.add(new String[] { obj[0].trim(), obj[1].trim(), obj[2].trim(), obj[3].trim() });
			if (personTransMap.containsKey(obj[0])) {
				personTransMap.get(obj[0])
						.add(Arrays.asList(new String[] { obj[1].trim(), obj[2].trim(), obj[3].trim() }));
			} else {
				List<List<String>> trans = new ArrayList<>();
				trans.add(Arrays.asList(new String[] { obj[1].trim(), obj[2].trim(), obj[3].trim() }));
				personTransMap.put(obj[0], trans);

			}
		}
		for (String[] entry : objs) {
			if (Integer.parseInt(entry[1]) > 3000) {
				results.add(entry[0]);
				personTransMap.remove(entry[0]);
				break;
			}
		}
		Comparator<List<String>> comparator_times = new Comparator<List<String>>() {

			@Override
			public int compare(List<String> o1, List<String> o2) {
				int i = Integer.parseInt(o2.get(2));
				int j = Integer.parseInt(o1.get(2));
				if (i < j) {
					return 1;
				} else if (i > j) {
					return -1;
				} else {
					return 0;
				}
			}

		};
		for (Map.Entry<String, List<List<String>>> entry : personTransMap.entrySet()) {
			List<List<String>> value = entry.getValue();
			Collections.sort(value, comparator_times);
			for (int i = 0; i < value.size(); i++) {
				List<String> li = value.get(i);
				if (i < value.size() - 1) {
					List<String> nextli = value.get(i + 1);
					if (!li.get(1).toLowerCase().equalsIgnoreCase(nextli.get(1).toLowerCase())) {
						int trans1Time = Integer.parseInt(li.get(2));
						int trans2Time = Integer.parseInt(nextli.get(2));
						if (trans2Time - trans1Time < 59) {
							results.add(entry.getKey());
						}

					}
				} else if (Integer.parseInt(li.get(0)) > 3000) {
					results.add(entry.getKey());
				}
			}
		}
		System.out.println("Result : " + results);
		return transactions;

	}

	public static void main(String[] args) {
		String[] transactions = { "Shilpa|500|California|63", "Tom|25|New York|615", "Krasi|9000|California|1230",
				"Tom|25|New York|1235", "Tom|25|New York|1238", "Shilpa|50|Michigan|1300", "Matt|90000|Gerogia|1305",
				"Jay|100000|Virginia|1310", "Krasi|49|Florida|1320", "Krasi|83|California|1325",
				"Shilpa|50|California|1350" };
		getSuspiciousList(transactions);

	}
}
