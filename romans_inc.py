#!/usr/bin/env python2
"""
Conversion of arabic numbers to roman numerals.

This implementation uses a rather brutish approach.  By defining an increment
function we can create all roman numerals inductively.
"""

import doctest


successors = { 'I': 'V',
	       'V': 'X',
	       'X': 'L',
	       'L': 'C',
	       'C': 'D',
 	       'D': 'M',
 	       'M': None,
	     }


predecessors = { 'I': None,
 	         'V': 'I',
	         'X': 'V',
	         'L': 'X',
	         'C': 'L',
 	         'D': 'C',
 	         'M': 'D',
	       }


def succ(numeral):
	return successors[numeral]


def pred(numeral):
	return predecessors[numeral]


def inc(r):
	"""
	>>> inc('I')
	'II'
	>>> inc('III')
	'IV'
	>>> inc('IV')
	'V'
	>>> inc('V')
	'VI'
	>>> inc('VI')
	'VII'
	>>> inc('VII')
	'VIII'
	>>> inc('VIII')
	'IX'
	>>> inc('X')
	'XI'
	>>> inc('XIV')
	'XV'
	>>> inc('XXXVIII')
	'XXXIX'
	>>> inc('XXXIX')
	'XL'
	>>> inc('LXXXIX')
	'XC'
	>>> r = 'I'
	>>> s = set()
	>>> while True:
	...     s.add(r)
	...	try:
	...     	r = inc(r)
	...	except Exception:
	...		break
	>>> len(s)
	3999
	"""

	r += 'I'
	while True:
		if r.endswith('IIII') \
		or r.endswith('XXXX') \
		or r.endswith('CCCC'):
			r = r[:-3] + succ(r[-1])
		elif r.endswith('MMMM'):
			raise Exception()
		elif r.endswith('IVI') \
		or r.endswith('IXI') \
		or r.endswith('XLX') \
		or r.endswith('XCX') \
		or r.endswith('CDC') \
		or r.endswith('CMC'):
			r = r[:-3] + r[-2]
		elif r.endswith('VIV') \
		or r.endswith('LXL') \
		or r.endswith('DCD'):
			r = r[:-3] + r[-2] + succ(r[-1])
		else:
			break
	return r


def dec(r):
	"""
	>>> dec('II')
	'I'
	>>> dec('IV')
	'III'
	>>> dec('V')
	'IV'
	>>> dec('VI')
	'V'
	>>> dec('IX')
	'VIII'
	>>> dec('X')
	'IX'
	>>> r = 'I'
	>>> for n in range(1,3999):
	...	assert(r == dec(inc(r)))
	...     r = inc(r)
	"""
	while True:
		if r.endswith('I'):
			r = r[:-1]
			break
		elif r.endswith('IV') \
		or r.endswith('XL') \
		or r.endswith('CD'):
			r = r[:-2] + 4 * r[-2]
		if r.endswith('IX') \
		or r.endswith('XC') \
		or r.endswith('CM'):
			r = r[:-2] + pred(r[-1]) + 4 * r[-2]
		elif r.endswith('X') \
		or r.endswith('C') \
		or r.endswith('M'):
			r = r[:-1] + pred(pred(r[-1])) + r[-1] + pred(pred(r[-1]))
		elif r.endswith('V') \
		or r.endswith('L') \
		or r.endswith('D'):
			r = r[:-1] + pred(r[-1]) + r[-1] + pred(r[-1])
	return r


def romanize(n):
	"""
	>>> romanize(23)
	'XXIII'
	>>> romanize(42)
	'XLII'
	"""
	r = 'I'
	for i in range(n-1):
		r = inc(r)
	return r



if __name__ == "__main__":

	doctest.testmod()
	
