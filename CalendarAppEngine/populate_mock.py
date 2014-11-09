import requests
import json

#url = "http://versatile-hull-742.appspot.com"
url = "http://localhost:8080"

payload = {'name': 'Advanced computer architecture', 'code' : 'CS-470', 'description' : 'mock description', 'numberOfCredits' : '4', 'professorName' : 'Pr. Ienne'}
r = requests.post(url + "/course/create", data=json.dumps(payload))

payload = {'name': 'Design technologies for integrated systems', 'code' : 'CS-472', 'description' : 'mock description', 'numberOfCredits' : '6', 'professorName' : 'Pr. De Micheli'}
r = requests.post(url + "/course/create", data=json.dumps(payload))

payload = {'name': 'Microelectronics for systems on chips', 'code' : 'CS-474', 'description' : 'mock description', 'numberOfCredits' : '4', 'professorName' : 'Pr. Beuchat / Pr. Piguet'}
r = requests.post(url + "/course/create", data=json.dumps(payload))

payload = {'name': 'Embedded systems', 'code' : 'CS-473', 'description' : 'mock description', 'numberOfCredits' : '4', 'professorName' : 'Pr. Beuchat'}
r = requests.post(url + "/course/create", data=json.dumps(payload))

payload = {'code' : 'CS-470', 'date' : '05.10.2014', 'startTime' : '12:00', 'endTime' : '14:00', 'type' : 'lecture', 'rooms' : {'0' : 'GCA331', '1' : 'GCB331'}}
r = requests.post(url + "/period/create", data=json.dumps(payload))

payload = {'code' : 'CS-470', 'date' : '08.10.2014', 'startTime' : '16:00', 'endTime' : '18:00', 'type' : 'exercises', 'rooms' : {'0' : 'GCA331', '1' : 'GCB331'}}
r = requests.post(url + "/period/create", data=json.dumps(payload))
