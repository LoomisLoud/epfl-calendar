import requests
import json

payload = {'name': 'Advanced computer architecture', 'code' : 'CS-470', 'description' : 'mock description', 'numberOfCredits' : '4', 'professorName' : 'Pr. Ienne'}
r = requests.post("http://localhost:8080/createCourse", data=payload)

payload = {'name': 'Design technologies for integrated systems', 'code' : 'CS-472', 'description' : 'mock description', 'numberOfCredits' : '6', 'professorName' : 'Pr. De Micheli'}
r = requests.post("http://localhost:8080/createCourse", data=payload)

payload = {'name': 'Microelectronics for systems on chips', 'code' : 'CS-474', 'description' : 'mock description', 'numberOfCredits' : '4', 'professorName' : 'Pr. Beuchat / Pr. Piguet'}
r = requests.post("http://localhost:8080/createCourse", data=payload)

payload = {'name': 'Embedded systems', 'code' : 'CS-473', 'description' : 'mock description', 'numberOfCredits' : '4', 'professorName' : 'Pr. Beuchat'}
r = requests.post("http://localhost:8080/createCourse", data=payload)

payload = {'code' : 'CS-470', 'date' : '05/10/2014', 'startTime' : '12:00', 'endTime' : '14:00', 'type' : 'lecture', 'rooms' : ['GCA331', 'GCB331']}
r = requests.post("http://localhost:8080/createPeriod", data=payload)

payload = {'code' : 'CS-470', 'date' : '08/10/2014', 'startTime' : '16:00', 'endTime' : '18:00', 'type' : 'exercises', 'rooms' : ['GCA331', 'GCB331']}
r = requests.post("http://localhost:8080/createPeriod", data=payload)