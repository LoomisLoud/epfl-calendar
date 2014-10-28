import webapp2
from google.appengine.ext import ndb

from Course import Course
from Period import Period

# Author : gilbrechbuhler
# Web service used to create a course entity in the database
class clearDatastore(webapp2.RequestHandler):
    def get(self):
        periodsList = Period.query()
        for period in periodsList:
            period.key.delete()
        courseList = Course.query()
        for course in courseList:
            course.key.delete()

application = webapp2.WSGIApplication([
    ('/clear/datastore', clearDatastore),
], debug=True)