#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Mon Jan 11 16:59:49 2021

@author: clarissebieder
"""

import json
import requests
import time

api_key = "BKZpCz91pJUXJ6iShPlT3Fsjh5s2pMSv"                    # key to use the Blablacar API
api_url = " https://public-api.blablacar.com/api/v3/trips"      # URL used for the request
departure_coordinates = '48.828247,2.28359 '                    # EXAMPLE of departure coordinates
arrival_coordinates = '47.747734,-3.366091 '                    # EXAMPLE of arrival coordinates
start_date = '2021-02-19T00:00:00'                              # EXAMPLE of start date : AAAA-MM-DDThh:mm:ss 
                                                                # A = year / M = month / D = day / h = hour / m = minute / s = second


payload = {'key' : api_key, 'from_coordinate' : departure_coordinates, 'to_coordinate' : arrival_coordinates, 'start_date_local': '2021-01-15T00:00:00'}
time.sleep(2)                                                   # time to wait to make the request : be sure we don't make more than 100 requests per minute
response = requests.get(api_url, params = payload)              # requests can parse directly
#parsed = response.json                                         # no needeed because response already parsed

#print(response.url)                                            # Test : be sure we have the great URL

#print (response.text)                                          # Print the response decoded given by the API 

print(response.headers["x-ratelimit-remaining-day"])            # Print how many request we can still make for today


"""

--- COMMENTAIRE ---


Éventuellement rajouter une lecture de fichiers pour les entrées : requêtes automatisées
Possible d'ajouter plus d'option dans la recherches d'itinéraires si besoin'

Ajout d'un time.sleep() en secondes pour s'assurer de ne pas faire trop de requêtes
NOTE : requests contient en interne une méthode pour parser et décoder les données



Data given by BBC:
    
    * "link"                      : gives the URL to redirect to the search results page on the BlaBlaCar website
    * "search_info"               : gives informations about the search :
        + "count"                 : The total number of trips, including full trips (those with no remaining seats)
        + "full_trip_count"       : The total number of full trips
    * "trips"                     : Detail of each trip :
        + "link"                  : The URL to redirect to the trip details page on the BlaBlaCar website.
        + "waypoints"             : The waypoints of the trip :
            - "date_time"         : date / hour of departure & arrival
            - "place"             : The details of the place :
                ."city"           : city of the departure / arrival
                ."address"        : address of the departure / arrival
                ."latitude"       : of the departure / arrival address
                ."longitude"      : of the departure / arrival address
                ."country_code"   : country of departure / arrival
        + "price"                 : how much cost the trip :
            - "amount"            : The total price to pay for the trip
            - "currency"          : The currency applied to the price
        + "vehicle"               : information about the vehicle :
            - "make"              : The make of the vehicle (Citroen, Peugeot...)
            - "model"             : The model of the vehicle precisely 
        
"""