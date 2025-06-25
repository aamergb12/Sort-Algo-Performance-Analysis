/*
Aamer Goual Belhamidi
axg210204
ASS3P1
Analysis of sorting techniques
CS3345.504
*/

import java.util.Arrays;
import java.util.Random;

/*
 stores the result of sorting with number of comparisons
 movements and time taken
 */
class SortResult {
    long comparisons;
    long movements;
    long timeMillis;

    SortResult(long comparisons, long movements, long timeMillis) {
        this.comparisons = comparisons;
        this.movements = movements;
        this.timeMillis = timeMillis;
    }

    // print method to output results in a clean format
    public void print(String sortName) {
        System.out.println(sortName);
        System.out.println("Comparisons: " + comparisons);
        System.out.println("Moves: " + movements);
        System.out.println("Time: " + timeMillis + " ms\n");
    }
}

//
// ------------------------ Insertion Sort ------------------------
//
class InsertionSort {
    public static SortResult insertionSort(int[] arr) {
        long comparisons = 0, movements = 0;
        long start = System.currentTimeMillis();

        // we start from index 1 and insert each element
        //  into the sorted portion to the left
        for (int i = 1; i < arr.length; i++) {
            int key = arr[i];
            int j = i - 1;

            // shift elements that are greater than key 
            // to one position ahead
            while (j >= 0) {
                comparisons++;
                if (arr[j] > key) {
                    arr[j + 1] = arr[j];
                    movements++;
                    j--;
                } else {
                    break;
                }
            }

            // place the key in its correct position
            arr[j + 1] = key;
            movements++;
        }

        long end = System.currentTimeMillis();
        return new SortResult(comparisons, movements, end - start);
    }
}

//
// ------------------------ Selection Sort ------------------------
//
class SelectionSort {
    public static SortResult selectionSort(int[] arr) {
        long comparisons = 0, movements = 0;
        long start = System.currentTimeMillis();

        // outer loop runs for each position in the array
        for (int i = 0; i < arr.length - 1; i++) {
            int minIdx = i;

            // find the smallest element in the remaining 
            // unsorted part
            for (int j = i + 1; j < arr.length; j++) {
                comparisons++;
                if (arr[j] < arr[minIdx]) {
                    minIdx = j;
                }
            }

            // swap the smallest element found with the first
            //  unsorted element
            if (minIdx != i) {
                int temp = arr[minIdx];
                arr[minIdx] = arr[i];
                arr[i] = temp;
                movements += 3; // count all three assignments 
                                // as movements
            }
        }

        long end = System.currentTimeMillis();
        return new SortResult(comparisons, movements, end - start);
    }
}

//
// ------------------------ Quick Sort ------------------------
//
class QuickSort {
    private static long comparisons, movements;
    private static final Random rand = new Random();

    public static SortResult quickSort(int[] arr) {
        comparisons = 0;
        movements = 0;
        long start = System.currentTimeMillis();

        quickSort(arr, 0, arr.length - 1);

        long end = System.currentTimeMillis();
        return new SortResult(comparisons, movements, end - start);
    }

    // the recursive part of quick sort
    private static void quickSort(int[] arr, int low, int high) {
        if (low < high) {
            // use a random pivot to avoid worst case scenario
            //i did this cuz it was kept doing forever
            int pi = randomizedPartition(arr, low, high);

            // sort the elements before and after partition
            quickSort(arr, low, pi - 1);
            quickSort(arr, pi + 1, high);
        }
    }

    // Pick a random pivot, swap it to the end, and partition
    private static int randomizedPartition(int[] arr, int low, int high) {
        int pivotIndex = low + rand.nextInt(high - low + 1);
        swap(arr, pivotIndex, high); // Move pivot to the end
        return partition(arr, low, high);
    }

    // partition the array and return
    // the index where pivot finally goes
    private static int partition(int[] arr, int low, int high) {
        int pivot = arr[high]; // Pivot is at the end
        int i = low - 1; // Index of smaller element

        for (int j = low; j < high; j++) {
            comparisons++;
            if (arr[j] < pivot) {
                i++;
                swap(arr, i, j);
                movements += 3;
            }
        }

        swap(arr, i + 1, high); // put pivot in the right position
        movements += 3;
        return i + 1;
    }

    // swap the helper function
    private static void swap(int[] arr, int i, int j) {
        int temp = arr[i]; arr[i] = arr[j]; arr[j] = temp;
    }
}

//
// ------------------------ Merge Sort ------------------------
//
class MergeSort {
    private static long comparisons, movements;

    public static SortResult mergeSort(int[] arr) {
        comparisons = 0;
        movements = 0;
        long start = System.currentTimeMillis();

        mergeSort(arr, 0, arr.length - 1);

        long end = System.currentTimeMillis();
        return new SortResult(comparisons, movements, end - start);
    }

    // recursive part: divide the array
    private static void mergeSort(int[] arr, int left, int right) {
        if (left < right) {
            int mid = (left + right) / 2;
            mergeSort(arr, left, mid);
            mergeSort(arr, mid + 1, right);
            merge(arr, left, mid, right); // merge the sorted halves
        }
    }

    // merge the two sorted halves into single sorted part of it
    private static void merge(int[] arr, int left, int mid, int right) {
        // Copy the halves into temp arrays
        int[] L = Arrays.copyOfRange(arr, left, mid + 1);
        int[] R = Arrays.copyOfRange(arr, mid + 1, right + 1);
        movements += L.length + R.length; 
        // count the movements for copying

        int i = 0, j = 0, k = left;

        // compare and merge
        while (i < L.length && j < R.length) {
            comparisons++;
            if (L[i] <= R[j]) {
                arr[k++] = L[i++];
            } else {
                arr[k++] = R[j++];
            }
            movements++;
        }

        // copy the rest of the elements
        while (i < L.length) {
            arr[k++] = L[i++];
            movements++;
        }

        // copy the rest of the elements agaubn 
        while (j < R.length) {
            arr[k++] = R[j++];
            movements++;
        }
    }
}

//
// ------------------------ Main Test Class ------------------------
//
public class SortingAnalysis {
    public static void main(String[] args) {
        int size = 50000;

        // make up test data for each data type
        int[] inOrder = generateInOrderArray(size);
        int[] reverseOrder = generateReverseOrderArray(size);
        int[] randomOrder = generateRandomArray(size);

        // run all sorts on InOrder data
        System.out.println("inOrder:");
        runAllSorts(inOrder);

        // run all sorts on ReverseOrder data
        System.out.println("reverseOrder:");
        runAllSorts(reverseOrder);

        // run all sorts on RandomOrder data
        System.out.println("randomOrder:");
        runAllSorts(randomOrder);
    }

    // run all 4 sorting algorithms then print performance og it
    public static void runAllSorts(int[] original) {
        int[] arr;

        arr = original.clone();
        SortResult result = InsertionSort.insertionSort(arr);
        result.print("Insertion Sort");

        arr = original.clone();
        result = SelectionSort.selectionSort(arr);
        result.print("Selection Sort");

        arr = original.clone();
        result = QuickSort.quickSort(arr);
        result.print("Quick Sort");

        arr = original.clone();
        result = MergeSort.mergeSort(arr);
        result.print("Merge Sort");
    }

    // create a sorted array
    public static int[] generateInOrderArray(int size) {
        int[] arr = new int[size];
        for (int i = 0; i < size; i++) arr[i] = i;
        return arr;
    }

    // create a reverse-sorted array 
    public static int[] generateReverseOrderArray(int size) {
        int[] arr = new int[size];
        for (int i = 0; i < size; i++) arr[i] = size - i;
        return arr;
    }

    // create a randomly shuffled array 
    public static int[] generateRandomArray(int size) {
        int[] arr = new int[size];
        Random rand = new Random();
        for (int i = 0; i < size; i++) arr[i] = rand.nextInt(size);
        return arr;
    }
}
