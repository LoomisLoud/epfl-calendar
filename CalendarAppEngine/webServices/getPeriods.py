import webapp2
from google.appengine.ext import ndb
from webapp2_extras import json

from Course import Course
from Period import Period

# Author : gilbrechbuhler
# Web service used to return all periods for a course encoded as JSON
# To get a period, you need to know its parent's key and therefor you need to have its corresponding course.
# See createPeriod.py
class getPeriods(webapp2.RequestHandler):
    def get(self):
        self.response.headers['Content-Type'] = 'application/json'
        courseCodeGet = self.request.get('code')
        # The four next lines gets the course corresponding to the code in parameter.
        courseList = Course.query_by_code(courseCodeGet).fetch(1)
        course = None
        for c in courseList:
            course = c
        periodsList = Period.query_by_parent(course.key).fetch(5)
        periodsJSON = {}
        i = 0
        for p in periodsList:
            periodsJSON.update({str(i) : p.get_as_json()})
            i = i + 1
        self.response.out.write(json.encode(periodsJSON))


application = webapp2.WSGIApplication([
    ('/period/get', getPeriods),
], debug=True)