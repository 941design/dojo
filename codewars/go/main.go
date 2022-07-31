package main

import (
	"fmt"
)

// https://www.codewars.com/kata/5556282156230d0e5e000089/train/go
func DNAtoRNA(dna string) string {
	t := ""
	for _, c := range dna {
		if c == 'T' {
			t += "U"
		} else {
			t += string(c)
		}
	}
	return t
}

// https://www.codewars.com/kata/55f9bca8ecaa9eac7100004a/train/go
func Past(h, m, s int) int {
	return ((((h * 60) + m) * 60) + s) * 1000
}

// https://www.codewars.com/kata/59d9ff9f7905dfeed50000b0/train/go
func solve(slice []string) []int {
	result := make([]int, len(slice))
	for i, word := range slice {
		result[i] = solveWord(word)
	}
	return result
}
func solveWord(word string) int {
	sum := 0
	for i, c := range word {
		if int(c)&95-65 == i {
			sum += 1
		}
	}
	return sum
}

// https://www.codewars.com/kata/55d24f55d7dd296eb9000030
func Summation(n int) int {
	sum := 0
	for {
		sum += n
		n -= 1
		if n == 0 {
			return sum
		}
	}
}

func strcp(s string) string {
	t := ""
	for _, c := range s {
		t += string(c)
	}
	return t
}

func main() {
	fmt.Println(ss(10))
}
