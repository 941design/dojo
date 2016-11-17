import doctest

ENGLISH_NUMBERS = {
    1: 'one',
    2: 'two',
    3: 'three',
    4: 'four',
    5: 'five',
    6: 'six',
    7: 'seven',
    8: 'eight',
    9: 'nine',
    10: 'ten',
    11: 'eleven',
    12: 'twelve',
    13: 'thirteen',
    14: 'fourteen',
    15: 'fifteen',
    16: 'sixteen',
    17: 'seventeen',
    18: 'eighteen',
    19: 'nineteen',
    20: 'twenty',
    30: 'thirty',
    40: 'forty',
    50: 'fifty',
    60: 'sixty',
    70: 'seventy',
    80: 'eighty',
    90: 'ninety',
    100: 'hundred',
    10**3: 'thousand',
    10**6: 'million',
    10**9: 'billion',
    10**12: 'trillion',
    10**15: 'quadrillion',
    10**18: 'quintillion',
    10**21: 'sextillion',
    10**24: 'septillion',
}


def crop(n, upper=1, lower=0):
    """
    >>> crop(0)
    0
    >>> crop(0, 1)
    0
    >>> crop(0, 1, 1)
    0
    >>> crop(1)
    1
    >>> crop(123)
    3
    >>> crop(123, 2, 0)
    23
    >>> crop(123, 2, 1)
    2
    >>> crop(123, 3, 2)
    1
    >>> crop(123, 3, 3)
    0
    >>> crop(123, 6, 0)
    123
    >>> crop(123, 6, 3)
    0
    >>> crop(12000, 3, 0)
    0
    >>> crop(12345678, 6, 3)
    345
    """
    # return [n % 10**x // 10**(x-1) for x in range(upper, lower, -1)]
    return n % 10**upper // 10**lower
    

def translate(n):
    return ENGLISH_NUMBERS[n]


def int_to_english(n):
    """
    >>> int_to_english(25161045656)
    'twenty five billion one hundred sixty one million forty five thousand six hundred fifty six'
    """
    return " ".join(int_to_english_list(n))
    

def int_to_english_list(n):
    """
    >>> int_to_english_list(1)
    ['one']
    >>> int_to_english_list(10)
    ['ten']
    >>> int_to_english_list(11)
    ['eleven']
    >>> int_to_english_list(19)
    ['nineteen']
    >>> int_to_english_list(21)
    ['twenty', 'one']
    >>> int_to_english_list(40)
    ['forty']
    >>> int_to_english_list(47)
    ['forty', 'seven']
    >>> int_to_english_list(99)
    ['ninety', 'nine']
    >>> int_to_english_list(100)
    ['one', 'hundred']
    >>> int_to_english_list(101)
    ['one', 'hundred', 'one']
    >>> int_to_english_list(345)
    ['three', 'hundred', 'forty', 'five']
    >>> int_to_english_list(1000)
    ['one', 'thousand']
    >>> int_to_english_list(12356)
    ['twelve', 'thousand', 'three', 'hundred', 'fifty', 'six']
    >>> int_to_english_list(301000)
    ['three', 'hundred', 'one', 'thousand']
    >>> int_to_english_list(10**24)
    ['one', 'septillion']
    >>> int_to_english_list(10**24 + 23)
    ['one', 'septillion', 'twenty', 'three']
    """
    l = []
    for exp in range(0, 26, 3):
        
        # x, y, z = crop(n, exp+3, exp)

        
        # if y and y < 2:
        #     l = [ translate(y*10 + z) ] + l
        # elif y > 2:
        #     l = [ translate(z) ] + l
        #     l = [ translate(y) ] + l
        # if x:
        #     l = [ translate(100) ] + l
        #     l = [ translate(x) ] + l
        # if exp != 0:
        #     l += [ translate(10**exp) ]

        part = crop(n, exp+3, exp)
        x, y, z = part // 100, part % 100 // 10, part % 10

        if not part:
            continue

        if exp > 0:
            l = [ translate(10**exp) ] + l
            
        if y < 2 and y+z > 0:
            l = [ translate(y*10 + z) ] + l

        else:
            if z > 0:
                l = [ translate(z) ] + l
            if y > 0:
                l = [ translate(y*10) ] + l

        if x > 0:
            l = [ translate(100) ] + l
            l = [ translate(x) ] + l

    return l
