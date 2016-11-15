# https://www.codewars.com/kata/finding-an-appointment

from functools import reduce


def to_minutes(s, offset_h=9):
    """
    >>> to_minutes('09:00')
    0
    >>> to_minutes('10:10')
    70
    """
    return (int(s[:2]) - offset_h) * 60 + int(s[3:])

def from_minutes(i, offset_h=9):
    """
    >>> from_minutes(0)
    '09:00'
    >>> from_minutes(70)
    '10:10'
    """
    return '{0:02d}:{1:02d}'.format((i // 60) + offset_h, i % 60)

def schedule_to_bitstring(schedule):
    """Returns int, where each bit represents a minute, and is switched on
    if there already is an appointment.

    """
    appointments = [to_bitstring(to_minutes(start), to_minutes(end)-1) for (start, end) in schedule]
    return reduce(int.__or__, appointments) if appointments else 0

def get_start_time(schedules, duration):
    """
    >>> schedules = [
    ... [['09:00', '11:30'], ['13:30', '16:00'], ['16:00', '17:30'], ['17:45', '19:00']],
    ... [['09:15', '12:00'], ['14:00', '16:30'], ['17:00', '17:30']],
    ... [['11:30', '12:15'], ['15:00', '16:30'], ['17:45', '19:00']]]
    >>> get_start_time(schedules, 60)
    '12:15'
    >>> get_start_time(schedules, 75)
    '12:15'
    >>> schedules = [
    ... [['09:09', '11:27'], ['12:14', '13:41'], ['15:16', '17:17'], ['17:32', '18:50']], 
    ... [['10:38', '12:06'], ['13:39', '15:08'], ['17:23', '17:26'], ['18:02', '18:26']]]
    >>> get_start_time(schedules, 10)
    '18:50'
    >>> schedules = [
    ... [['11:21', '12:42'], ['12:51', '13:20'], ['17:51', '17:53'], ['18:07', '18:11']], 
    ... [['10:07', '10:39'], ['10:41', '11:03'], ['12:21', '12:22'], ['15:49', '16:11'], ['17:29', '17:54']], 
    ... [['09:41', '09:57'], ['10:03', '10:14'], ['10:32', '10:39'], ['10:56', '11:17'], ['11:23', '11:41'], ['11:59', '12:03'], ['12:28', '12:45'], ['17:19', '17:27'], ['18:56', '18:57']], 
    ... [['09:48', '12:26'], ['15:41', '15:59'], ['18:50', '18:57']], 
    ... [['09:37', '11:19'], ['11:27', '13:37'], ['16:29', '17:41']]]
    >>> get_start_time(schedules, 125)
    """
    unavailables = reduce(int.__or__, map(schedule_to_bitstring, schedules))
    return possible_appointment(unavailables, duration)

def possible_appointment(unavailable, duration):
    appointment = to_bitstring(0, duration - 1)
    while appointment < 2**(60 * 10):
        if not unavailable & appointment:
            return from_minutes(bin(appointment)[2:].count('0'))
        appointment <<= 1

def to_bitstring(start, end):
    """
    >>> bin(to_bitstring(0, 0))
    '0b1'
    >>> bin(to_bitstring(0, 1))
    '0b11'
    >>> bin(to_bitstring(0, 2))
    '0b111'
    >>> bin(to_bitstring(1, 1))
    '0b10'
    >>> bin(to_bitstring(1, 2))
    '0b110'
    """
    return int('1' * (end - start + 1) + '0' * start, 2)
    
