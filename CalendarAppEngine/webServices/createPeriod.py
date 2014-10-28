import webapp2
import datetime
from google.appengine.ext import ndb
from webapp2_extras import json

from Course import Course
from Period import Period

# Author : gilbrechbuhler
# Web service used to create a period entity in the database
# A period's parent is the corresponding course (so we set its key as period's parent)
class createPeriod(webapp2.RequestHandler):
    def post(self):
        values = json.decode(self.request.body)
        courseCodeGet = values['code']
        dateGet = values['date']
        startTimeGet = values['startTime']
        endTimeGet = values['endTime']
        periodTypeGet = values['type']
        rooms = values['rooms']
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
        i = 0
        for r in rooms:
            period.rooms.append(rooms[str(i)])
            i = i + 1
        period.put()
        course.periods.append(period.key)
        course.put()


application = webapp2.WSGIApplication([
    ('/period/create', createPeriod)
], debug=True)