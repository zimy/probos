package me.zimy.probos;

import org.springframework.stereotype.Service;

/**
 * @author Wikipedia
 */
@Service
public class SortService {
    /**
     * Counting access, math, comparison, assignment
     * 2
     * 1
     * (1, 2) * (0 .. n-1)
     * 1
     * 2
     * (1, 2)*(i..n)
     * 3
     * X--actual-resettings
     * 2
     * 3
     * 2
     * <p/>
     * So actually we do 3+(n-1)*13+6*(n+1)*n/2+X--actual-resetting
     *
     * @param numbers input array
     */
    public int selectionSort(double[] numbers) {
        int SERVICE = 0;
        int min;
        double temp;
        int mainTermination = numbers.length - 1;
        for (int index = 0; index < mainTermination; index++) {
            min = index;
            for (int scan = index + 1; scan < numbers.length; scan++)
                if (numbers[scan] < numbers[min]) {
                    min = scan;
                    SERVICE++;
                }
            temp = numbers[min];
            numbers[min] = numbers[index];
            numbers[index] = temp;
        }
        return SERVICE;
    }

    /**
     * 1
     * (1, 2)
     * 2
     * 2
     * (4)
     * 4
     * 2
     * 2
     * <p/>
     * So actually we do 1+7*(n-1)+(n-1)*(4)+12*X--actual-resetting
     *
     * @param arr input array
     */
    public int insertionSort(int[] arr) {
        int SERVICE = 0;
        for (int i = 1; i < arr.length; i++) {
            int currElem = arr[i];
            int prevKey = i - 1;
            while (prevKey >= 0 && arr[prevKey] > currElem) {
                arr[prevKey + 1] = arr[prevKey];
                arr[prevKey] = currElem;
                prevKey--;
                SERVICE++;
            }
        }
        return SERVICE;
    }
}
