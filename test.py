import requests

payload = {'code': 'CS-205', 'name': 'SE'}
r = requests.post("http://localhost:8080/", data=payload)
print(r.text)

payload = {'code': 'CS-215', 'name': 'test'}
r = requests.post("http://localhost:8080/", data=payload)
print(r.text)

payload = {'code': 'CS-225', 'name': 'java class'}
r = requests.post("http://localhost:8080/", data=payload)
print(r.text)

payload = {'code': 'CS-235', 'name': 'Mobile Networks'}
r = requests.post("http://localhost:8080/", data=payload)
print(r.text)