package org;

class Main {
	public static void main(String []args) {
		int array[] = {2, 1, 4, 5, 3};

		insertion_sort(array);

		for (int i = 1; i <= array.length; i++) {
    System.out.println(array[i]);

		}
	}

	public static void insertion_sort(int array[]) {
			for (int j = 2; j < array.length; j++) {
				int chave = array[j];
				int i = j - 1;
				
				while (i > 0 && array[i] > chave) {
				array[i + 1] = array[i];
				i = i - 1;
				}
			array[i + 1] = chave;
			}
		}
}
