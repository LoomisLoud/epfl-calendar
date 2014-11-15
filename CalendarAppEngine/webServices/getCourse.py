import webapp2
from google.appengine.ext import ndb
from webapp2_extras import json

from Course import Course
from Period import Period
from Log import Log

# Author : gilbrechbuhler
# Web service used to create a course entity in the database
class getCourse(webapp2.RequestHandler):
    def get(self):
        get_values = self.request.GET
        courseJson = {}
        passedParam = ""
        if ('name' in get_values):
            name = self.request.get('name')
            passedParam = name
            courseList = Course.query_by_name(name).fetch(1)
        elif ('code' in get_values):
            code = self.request.get('code')
            passedParam = code
            courseList = Course.query_by_code(code).fetch(1)
        self.response.headers['Content-Type'] = 'application/json'
        self.response.content_type_params = {'charset': 'utf8'}
        if (len(courseList) >= 1):
            for course in courseList:   
                courseJson = course.get_as_json()
        else:
            log = Log(logText = "Course name : " + passedParam)
            log.put()
        self.response.out.write(json.encode(courseJson))



application = webapp2.WSGIApplication([
    ('/course/get', getCourse),
], debug=True)
