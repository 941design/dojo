import unittest
import random as rnd

"""Several officials, managers and even athletes of your national
football league have been charged with corruption allegations.  The
upcoming season is at stake.  With only a few days to go, the season's
pairings have not yet been established.  In search for trustworthy
individuals you were assigned this important task.

Implement a function 'schedule' that takes a list of teams and returns
a list containing a season's pairings, such that the following
criteria are met:

+ the number of teams must be even.
+ every team competes against all other teams exactly once.
+ the difference between home and away games is exactly 1 for all
  teams, i.e. every team either has one home match more than it has away
  matches or vice versa.

The function takes a single argument of the form:
["team a", "team b", ..]

The function's return value has the following structure:
[[("team a", "team b"), ..], ..], where a is the home team and b the contender.  The
inner list represents a single day and contains all pairings for that
day. The outer list contains all days of the season.

A valid example for a league of four teams would be
>>> schedule(['a', 'b', 'c', 'd'])
[[('d', 'a'), ('c', 'b')], [('b', 'a'), ('d', 'c')], [('a', 'c'), ('b', 'd')]]

"""

BUNDESLIGA = ['RB Leipzig',
              'Bayern Muenchen',
              'Hertha BSC',
              'TSG Hoffenheim',
              'Eintracht Frankfurt',
              'Borussia Dortmund',
              '1. FC Koeln',
              'FC Schalke 04',
              'Bayer 04 Leverkusen',
              '1. FSV Mainz 05',
              'SC Freiburg',
              'FC Augsburg',
              'Bor. Moenchengladbach',
              'Werder Bremen',
              'VfL Wolfsburg',
              'SV Darmstadt 98',
              'Hamburger SV',
              'FC Ingolstadt 04']

CITIES = ['Madrid', 'Barcelona', 'Seville', 'Berlin', 'Dortmund', 'Munich', 'Chelsea',
          'London', 'Manchester', 'Paris', 'Marseille', 'Lyon', 'Milan', 'Rome',
          'Lisboa', 'Porto', 'Monaco', 'Eindhoven', 'Rotterdam', 'Amsterdam', 'Kiew',
          'Moscow', 'Odessa']

ADDITIONS = ['{}', 'FC {}', 'SV {} 05', '{} United', '{} Rangers', 'Olympic {}',
             'PSV {}', 'AC {}', 'Borussia {}', 'Real {}', 'Ajax {}', 'Dynamo {}',
             'Deportivo {}']


A,B,C,D,E,F,G,H = "abcdefgh"


class ScheduleTest(unittest.TestCase):


    def randomLeagues(self, n, min=14, max=52):
        return [self.randomLeague(min, max) for _ in range(n)]


    def randomLeague(self, min, max):
        league = set()
        league_size = rnd.randint(min, max) // 2 * 2
        
        while len(league) < league_size:
            league.add(rnd.choice(ADDITIONS).format(rnd.choice(CITIES)))

        return list(league)

    
    def assertStructureCorrect(self, league):
        season = schedule(league)

        self.assertIsInstance(season, list, 'must return list, got {}'.format(type(season)))

        for day in season:

            self.assertIsInstance(day, list, 'days in season must be list, got {}'.format(type(day)))

            for pairing in day:
                self.assertIsInstance(pairing, tuple, 'pairings in day must be tuple, got {}'.format(type(pairing)))
                self.assertEqual(len(pairing), 2, 'pairings must consist of two teams, was {}'.format(pairing))

        self.assertEqual(set(len(day) for day in season), {(len(league) // 2)}, 'number of games per day inconsistent')

        self.assertEqual(len(season), len(league) - 1, 'incorrect number of days per season')


    def assertAllTeamsPlayingDaily(self, league):
        season = schedule(league)

        is_playing = lambda day, team: any(team == a or team == b for a, b in day)

        for team in league:
            for i, day in enumerate(season):
                self.assertTrue(is_playing(day, team), 'team \'{}\' not playing on day {}'.format(team, i))


    def assertAllTeamsPlayingOnlyOncePerDay(self, league):
        season = schedule(league)

        games = lambda day, team: sum(team == a or team == b for a, b in day)

        for team in league:
            for i, day in enumerate(season):
                self.assertEqual(games(day, team), 1, 'team \'{}\' playing more than once on day {}'.format(team, i))


    def assertHomeAwayDifferenceCorrect(self, league):
        season = schedule(league)

        for team in league:

            home = sum(team == a for day in season for a, _ in day)
            away = sum(team == b for day in season for _, b in day)

            self.assertEqual(home + away, len(league) - 1)  # TODO - should be caught by other tests
            self.assertEqual(abs(home - away), 1, 'incorrect home game to away game ratio {}/{}'.format(home, away))


    def assertNoDuplicatePairings(self, league):
        season = schedule(league)

        all_pairings = sorted(tuple(sorted(pairing)) for day in season for pairing in day)

        for i, pairing in enumerate(all_pairings[:-1]):
            other = all_pairings[i+1]
            self.assertNotEqual(pairing, other, 'teams compete more than once: \'{}\' \'{}\''.format(*pairing))

        
    def assertAll(self, league):        
        self.assertStructureCorrect(league)
        self.assertAllTeamsPlayingDaily(league)
        self.assertAllTeamsPlayingOnlyOncePerDay(league)
        self.assertHomeAwayDifferenceCorrect(league)
        self.assertNoDuplicatePairings(league)

        
    def test_empty(self):
        self.assertEqual(schedule([]), [])


    def test_odd(self):
        self.assertRaises(Exception, lambda: schedule(['foobar']))


    def test_league_of_2(self):
        self.assertIn(schedule([A, B]), ([[(A, B)]], [[(B, A)]]))

        
    def test_league_of_4(self):
        league = [A, B, C, D]
        self.assertAll(league)

        
    def test_league_of_6(self):
        league = [A, B, C, D, E, F]
        self.assertAll(league)

        
    def test_league_of_8(self):
        league = [A, B, C, D, E, F, G, H]
        self.assertAll(league)        

    def test_bundesliga(self):
        self.assertAll(BUNDESLIGA)
        
    def test_random(self):
        for league in self.randomLeagues(50):
            self.assertAll(league)


def schedule(teams):
    """Returns lists of pairings per day for the entire season.

    In a season all teams compete with each other, hence season length
    (and also the length of the returned list) is len(teams) - 1.

    Each pairing is returned as a tuple: (home, away).  Such that the
    overall structure becomes: [[(a, b), ..], ..].

    Raises ValueError if len(teams) is not an even number.

    """
    league_size = len(teams)
    season_length = league_size - 1
    
    if league_size % 2 != 0:
        raise ValueError('invalid league size')

    return [pairings(day, teams) for day in range(season_length)]


def pairings(day, teams):
    """Returns list of all pairings for a given day.

    """
    league_size = len(teams)
    season_length = league_size - 1
    pairings = []
    
    def next_pairing(day, season_length, i, *more):
        """Returns tuple of next two indices.

        Idea taken from:
        https://de.wikipedia.org/wiki/Spielplan_(Sport)

        Pairings are determined based on the teams index numbers i, k.
        Generally two teams pair if their index-sum (i + k) divided by
        the season's length yields the day's index as a remainder:  
        (i + k) % season_length == day. 
        
        Although, one modification needs to be made, as the method
        above may lead to either collisions or no match.  For example:
        In a league of 6 teams day 1 yields pairings [(0, 1), (2, 4)],
        but indices 3, and 5 cannot be matched.  Also in a league of 6
        teams day 2 yields the conflicting pairing (0, 2) and (2, 5).

        The solution is to exclude the last element from iteration.
        The left out element serves as a wildcard for otherwise
        unmatched indices and prevents conflicting matches.  With that
        adaption, for all days there is exactly one index that cannot
        be matched except with the wildcard (Proof?).  Each index
        number is skipped exactly once (Proof?).  Because each index
        is skipped once the skipped elements can be paired with the
        excluded element to arrive at a consistent pairing.

        Home and away are determined by the pairings sum % 2, which is
        evenly distributed.

        """
        for k in more[:-1]:
            if (i + k) % season_length == day:
                return (i, k) if (i + k) % 2 == 0 else (k, i)
        k = more[-1]
        return (i, k) if (i + k) % 2 == 0 else (k, i)
    
    indices = list(range(league_size))
    while indices:
        a, b = next_pairing(day, season_length, *indices)
        pairings.append((teams[a], teams[b]))
        indices.remove(a)
        indices.remove(b)
        
    return pairings
