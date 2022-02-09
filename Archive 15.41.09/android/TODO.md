
# Some rules to keep in mind
1. Put all strings in the string.xml file (it helps a lot when translating the app to others langages).

2. Organise strings in string.xml, menus, by categories. Example : messages in popups, buttons labels, error messages ..., menu items in activity 1, menu items in activity 2, ... .

3. Use separated threads to perform all heavy operations (like network communication, map processing, sound recording, ...).

4. Use a ViewModel (+LiveData) to store every activities data. Like that we don't lose (or reload) them when the activity is resumed or restarted.

5. Give precise description of functions and classes to be use outside of submodules.

6. Use fragments for components to be use several times in the code. So that it's faster to make changes.


# Android module to do list

## Communication (Cronet)
Contains all classes used to issue request to server and handle responses.

* Implement the pipelines for core communication fonctionalities:
  * car ride request 
  * covoit request
* Test that everything work fine before implementing more generic fonctionalities. It include testing :
  * All types of errors (Network, Server and function errors).
  * Try to find unhandled possible errors.
* Communicate with people of the server module to syncronise things.

## Local database (ORMLite)
Contains the local database specification and a handler to communicate with it.

* Test the current code of the database (with randomly generated datas of all types).

## Map display
Contains the graphic components (Fragment) use to display a map and draw path on it.
* Get to know the useful libraries we can use :
  * Leaflet
  * Osmdroid

## Location handling
Contains a class (and all its needed classes) to follow the device location.
* Try the google api for that.

## Activities
Contains all activities in the project. These activities use all the submodules to give the user what he what.