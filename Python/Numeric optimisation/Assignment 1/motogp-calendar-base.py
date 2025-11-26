import unittest
import math
import csv
import random
import copy
import numpy as np
import pyswarms as ps
from simanneal import Annealer
from deap import base, creator, tools

# constants that define the likely hood of two individuals having crossover
# performed and the probability that a child will be mutated. needed for the
# DEAP library
CXPB = 0.5
MUTPB = 0.2

# define a spurious best solution and cost. We will update this as we update the cost of each particle
swarm_best_cost = 1000000
swarm_best_itinerary = []
itineraries = []
home = 11

# the unit tests to check that the simulation has been implemented correctly
class UnitTests (unittest.TestCase):
    # this will read in the track locations file and will pick out 5 fields to see if the file has been read correctly
    def testReadCSV(self):
        # read in the locations file
        rows = readCSVFile('track-locations.csv')

        # test that the corners and a middle value are read in correctly
        self.assertEqual('GP', rows[0][0])
        self.assertEqual('Valencia', rows[0][22])
        self.assertEqual('Temp week 52', rows[55][0])
        self.assertEqual('16.25', rows[55][22])
        self.assertEqual('12.5', rows[11][8])
    
    # this will test to see if the row conversion works. here we will convert the latitude rwo and will test 5 values
    # as we are dealing with floating point we will use almost equals rather than a direct equality
    def testRowToFloat(self):
        # read in the locations file and convert the latitude column to floats
        rows = readCSVFile('track-locations.csv')
        convertRowToFloat(rows, 2)

        # check that 5 of the values have converted correctly
        self.assertAlmostEqual(14.957883, rows[2][1], delta=0.0001)
        self.assertAlmostEqual(39.484786, rows[2][22], delta=0.0001)
        self.assertAlmostEqual(36.532176, rows[2][17], delta=0.0001)
        self.assertAlmostEqual(-38.502284, rows[2][19], delta=0.0001)
        self.assertAlmostEqual(36.709896, rows[2][5], delta=0.0001)

        # check that the conversion of a temperature row to floating point is also correct
        convertRowToFloat(rows, 5)

        # check that 5 of the values have converted correctly
        self.assertAlmostEqual(31.5, rows[5][1], delta=0.0001)
        self.assertAlmostEqual(16.5, rows[5][22], delta=0.0001)
        self.assertAlmostEqual(8.5, rows[5][17], delta=0.0001)
        self.assertAlmostEqual(23.5, rows[5][19], delta=0.0001)
        self.assertAlmostEqual(16.5, rows[5][5], delta=0.0001)
    
    # # this will test to see if the file conversion overall is successful for the track locations
    # # it will read in the file and will test a string, float, and int from 2 rows to verify it worked correctly
    def testReadTrackLocations(self):
        # read in the locations file
        rows = readTrackLocations()

        # check the name, latitude, and final temp of the first race
        self.assertEqual(rows[0][0], 'Thailand')
        self.assertAlmostEqual(rows[2][0], 14.957883, delta=0.0001)
        self.assertAlmostEqual(rows[55][0], 30.75, delta=0.0001)

        # check the name, longitude, and initial temp of the last race        
        self.assertEqual(rows[0][21], 'Valencia')
        self.assertAlmostEqual(rows[2][21], 39.484786, delta=0.0001)
        self.assertAlmostEqual(rows[4][21], 16, delta=0.0001)
    
    # # tests to see if the race weekends file is read in correctly
    def testReadRaceWeekends(self):
        # read in the race weekends file
        weekends = readRaceWeekends()

        # check that thailand is weekend 8 and valencia is weekend 45
        self.assertEqual(weekends[0], 8)
        self.assertEqual(weekends[21], 45)

        # check that Austria is weekend 32
        self.assertEqual(weekends[12], 32)

    # # this will test to see if the haversine function will work correctly we will test 4 sets of locations
    def testHaversine(self):
        # read in the locations file with conversion
        rows = readTrackLocations()

        # check the distance of Thailand against itself this should be zero
        self.assertAlmostEqual(haversine(rows, 0, 0), 0.0, delta=0.01)
        
        # check the distance of Thailand against Silverstone this should be 9632.57 km
        self.assertAlmostEqual(haversine(rows, 0, 6), 9632.57, delta=0.01)

        # check the distance of silverstone against mugello this should be 1283.1 Km
        self.assertAlmostEqual(haversine(rows, 6, 8), 1283.12, delta=0.01)

        # check the distance of mugello to the red bull ring this should be 445.06 Km
        self.assertAlmostEqual(haversine(rows, 8, 12), 445.06, delta=0.01)
    
    # # will test to see if the season distance calculation is correct using the 2025 calendar
    def testDistanceCalculation(self):
        # read in the locations & race weekends, generate the weekends, and calculate the season distance
        tracks = readTrackLocations()
        weekends = readRaceWeekends()
        
        # calculate the season distance using mugello as the home track as this will be the case for almost all of the teams we will use silverstone for the others
        self.assertAlmostEqual(calculateSeasonDistance(tracks, weekends, 8), 146768.1778, delta=0.0001)
        self.assertAlmostEqual(calculateSeasonDistance(tracks, weekends, 6), 151481.2754, delta=0.0001)
    
    # # will test that the temperature constraint is working this should fail as azerbijan should fail the test
    def testTempConstraint(self):
        # load in the tracks, race weekends, and the sundays
        tracks = readTrackLocations()
        weekends1 = [8, 10, 12, 14, 16, 18, 20, 22, 24, 25, 27, 28, 32, 33, 35, 36, 38, 39, 41, 42, 44, 45]
        weekends2 = [8, 10, 12, 14, 16, 18, 30, 22, 24, 25, 27, 28, 32, 33, 35, 36, 38, 39, 48, 42, 40, 41]

        # the test with the default calender should be false because of Great Britian at 17.25
        self.assertEqual(checkTemperatureConstraint(tracks, weekends1, 20, 35), False)
        self.assertEqual(checkTemperatureConstraint(tracks, weekends2, 20, 35), True)
        
    # # will test that we can detect a period for a summer shutdown in the prescribed weeks
    def testSummerShutdown(self):
        # weekend patterns the first has a summer shutdown the second doesn't
        weekends1 = [8, 10, 12, 14, 16, 18, 20, 22, 24, 25, 27, 28, 32, 33, 35, 36, 38, 39, 41, 42, 44, 45]
        weekends2 = [8, 10, 12, 14, 16, 18, 20, 22, 24, 25, 27, 28, 31, 33, 35, 36, 38, 39, 41, 42, 44, 45]

        # the first should pass and the second should fail
        self.assertEqual(checkSummerShutdown(weekends1), True)
        self.assertEqual(checkSummerShutdown(weekends2), False)

# implementation of an annealer that will attempt to come up with a better calendar. it will allow the free movement of weekends with the exception of
# monaco. bahrain and abu dhabi will open and close the season respectively. the summer shutdown should still be respected. Double and triple header weekends
# are permitted but for or more races in a row is not allowed.
class CalendarAnnealer(Annealer):

    # used to initialise the annealer the state here will be the list of race weekends and the locations. we take in all the extra parameters
    # to help calculate the distance and temperature requirements on the way
    def __init__(self, weekends, home, tracks):
        pass

    # used to make a move this will take two race weekends and will swap their locations as this is the smallest change we can make
    def move(self):
        pass
        
    # used to calculate the energy. smaller values represent smaller distances. A default value of 1,000,000 will be returned if the
    # temperature requirement is not satisfied
    def energy(self):
        pass

# class that will hold the weekends for genetic algorithms
class CalendarGA:
    def __init__(self):
        pass
        
# function that will calculate the total distance for the season assuming a given racetrack as the home racetrack
# the following will be assumed:
# - on a weekend where there is no race the team will return home
# - on a weekend in a double or triple header a team will travel straight to the next race and won't go back home
# - the preseason test will always take place in Bahrain
# - for the summer shutdown and off season the team will return home
def calculateSeasonDistance(tracks, weekends, home):
    pass

# # function that will calculate the season distance and will include the cost of penalties in the calculation
def calculateSeasonDistancePenalties(tracks, weekends, home, min, max):
    pass

# function that will check to see if the temperature constraint for all races is satisfied. The temperature
# constraint is that a minimum temperature of min degrees for the month is required for a race to run
def checkTemperatureConstraint(tracks, weekends, min, max):
    pass

# function that will check to see if there is a four week gap anywhere in july and august. we will need this for the summer shutdown.
# the way this is defined is that we have a gap of three weekends between successive races. this will be weeks 29, 30, and 31, they are not
# permitted to have a race during these weekends
def checkSummerShutdown(weekends):
    pass

# will go through the genetic code of this child and will make sure that all the required weekends are in it.
# it's highly likely that with crossover that there will be weekends missing and others duplicated. we will
# randomly replace the duplicated ones with the missing ones
def childGeneticCodeFix(child):
    pass

# function that will take in the set of rows and will convert the given row index into floating point values
# this assumes the header in the CSV file is still present so it will skip the first column
def convertRowToFloat(rows, row_index):
    pass

# function that will count how many elements in the given array are greater equal a specific value
def countGreaterEqual(array, value):
    pass

# function that will perform roulette wheel crossover to generate children
def crossoverStrategy(ind1, ind2):
    pass

# function that will evaluate the strategy for a stock
def evaluateStrategy(individual):
    pass

# function that will generate the initial itineraries for particle swarm optimisation
# this will take the initial solution and will shuffle it to create new solutions
def generateInitialItineraries(num_particles, initial_solution):
    pass

# function that will generate a shuffled itinerary. However, this will make sure that the bahrain, abu dhabi, and monaco
# will retain their fixed weeks in the calendar
def generateShuffledItinerary(weekends):
    pass

# function that will use the haversine formula to calculate the distance in Km given two latitude/longitude pairs
# it will take in an index to two rows, and extract the latitude and longitude before the calculation.
def haversine(rows, location1, location2):
    pass

# function that will initialise a strategy for the stocks. we will randomise all the weights here
def initIndividual(ind_class):
    pass

# function that will give us the index of the lowest temp below min. will return -1 if none found
def indexHighestTemp(tracks, weekends, max):
    pass

# function that will give us the index of the lowest temp below min. will return -1 if none found
def indexLowestTemp(tracks, weekends, min):
    pass

# function that will mutate an individual
def mutateIndividual(individual, indpb):
    pass

# objective function for particle swarm optimisation
def objectiveCalendar(particles):
    pass

# prints out the itinerary that was generated on a weekend by weekend basis starting from the preaseason test
def printItinerary(tracks, weekends, home):
    pass

# function that will take in the given CSV file and will read in its entire contents
# and return a list of lists
def readCSVFile(file):
    # the rows to return
    rows = []

    # open the file for reading and give it to the CSV reader
    csv_file = open(file)
    csv_reader = csv.reader(csv_file, delimiter=',')

    # read in each row and append it to the list of rows.
    for row in csv_reader:
        rows.append(row)

    # close the file when reading is finished
    csv_file.close()

    # return the rows at the end of the function
    return rows

# function that will read in the race weekends file and will perform all necessary conversions on it
def readRaceWeekends():
    pass

# function that will read the track locations file and will perform all necessary conversions on it.
# this should also strip out the first column on the left which is the header information for all the rows
def readTrackLocations():
    pass

# function that performs a roulette wheel randomisation on the two given values and returns the chosen on
def rouletteWheel(a, b):
    pass

# function that will take an itinearary and will swap the elements based on the values in the particle. If only one element is selected
# for swapping it will be randomly swapped with another element. if two or more are selected we will take those elements and shuffle them
def swapElements(itinerary, particle):
    pass

# function that will return a list of the indexes to be swaped according to the particle
def swapIndexes(particle):
    pass

# function that will take an itineary and will swap a pair of weekends without changing valencia
def swapPair(itinerary):
    pass

# function that will swap the values at the given index with another randomly chosen index
def swapIndex(itinerary, index):
    pass

# function that will run the simulated annealing case for shortening the distance seperately for both silverstone and monza
def SAcases():
    pass

# function that will run the genetic algorithms cases for all four situations
def GAcases():
    pass

# function that will run particle swarm optimisation in an attempt to find a solution
def PSOcases():
    pass

if __name__ == '__main__':
    # uncomment this run all the unit tests. when you have satisfied all the unit tests you will have a working simulation
    # you can then comment this out and move onto your SA and GA solutions
    unittest.main()

    # just to check that the itinerary printing mechanism works. we will assume that silverstone is the home track for this
    #weekends = readRaceWeekends()
    #print(generateShuffledItinerary(weekends))
    #tracks = readTrackLocations()
    #printItinerary(tracks, weekends, 11)

    # run the cases for simulated annealing
    #SAcases()

    # run the cases for genetic algorithms
    #GAcases()

    # run the cases for particle swarm optimisation
    #PSOcases()