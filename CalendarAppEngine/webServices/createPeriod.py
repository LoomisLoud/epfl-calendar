import webapp2
import datetime
from google.appengine.ext import ndb

from Course import Course
from Period import Period

# Author : gilbrechbuhler
# Web service used to create a period entity in the database
# A period's parent is the corresponding course (so we set its key as period's parent)
class createPeriod(webapp2.RequestHandler):
    def post(self):
        courseCodeGet = self.request.get('code')
        dateGet = self.request.get('date')
        startTimeGet = self.request.get('startTime')
        endTimeGet = self.request.get('endTime')
        periodTypeGet = self.request.get('type')
        #rooms = self.request.get('rooms')
        courseList = Course.query_by_code(courseCodeGet).fetch(1)
        course = None
        for c in courseList:
            course = c
        period = Period(parent=course.key)
        # Setup the period
        period.date = datetime.datetime.strptime(dateGet, '%d/%m/%Y').date()
        period.startTime = datetime.datetime.strptime(startTimeGet, '%H:%M').time()
        period.endTime = datetime.datetime.strptime(endTimeGet, '%H:%M').time()
        period.periodType = periodTypeGet
        period.put()
        course.periods.append(period.key)
        course.put()


application = webapp2.WSGIApplication([
    ('/createPeriod', createPeriod)
], debug=True)