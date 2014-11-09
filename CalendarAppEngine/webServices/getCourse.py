import webapp2
from google.appengine.ext import ndb
from webapp2_extras import json

from Course import Course
from Period import Period

# Author : gilbrechbuhler
# Web service used to create a course entity in the database
class getCourse(webapp2.RequestHandler):
    def get(self):
        get_values = self.request.GET
        courseJson = {}
        if ('name' in get_values):
            name = self.request.get('name')
            courseList = Course.query_by_name(name).fetch(1)
        elif ('code' in get_values):
            code = self.request.get('code')
            courseList = Course.query_by_code(code).fetch(1)
        self.response.headers['Content-Type'] = 'application/json'
        if (len(courseList) >= 1):
            for course in courseList:   
                courseJson = course.get_as_json()
                periods = Period.query_by_parent(course.key).fetch(4)
        self.response.out.write(json.encode(courseJson))



application = webapp2.WSGIApplication([
    ('/course/get', getCourse),
], debug=True)