import array

a = array.array('B', [1, 3, 5, 7, 9, 11, 13])


def binary_search(arr, b):
    low = -1
    high = len(arr)
    guess = len(arr) // 2
    while arr[guess] != b:
        if arr[guess] < b:
            low = guess
        if arr[guess] > b:
            high = guess
        if high - low == 1:
            return None
        guess = (low + high) // 2
    return guess


print(binary_search(a, 13))
