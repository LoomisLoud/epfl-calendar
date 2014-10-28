import webapp2
from google.appengine.ext import ndb
from webapp2_extras import json

from Course import Course

# Author : gilbrechbuhler
# Web service used to create a course entity in the database
class createCourse(webapp2.RequestHandler):
    def post(self):
        values = json.decode(self.request.body)
        codeGet = values['code']
        if not(Course.isCodeUnique(codeGet)):
            self.response.headers['Content-Type'] = 'text/plain'
            self.response.write('error')
        else:
            nameGet = values['name']
            #descriptionGet = values('description')
            descriptionGet = ''
            numberOfCreditsGet = values['numberOfCredits']
            professorNameGet = values['professorName']
            course = Course(name = nameGet, periods = [], code = codeGet, description = descriptionGet, 
                numberOfCredits = int(numberOfCreditsGet), professorName = professorNameGet)
            course.put()


application = webapp2.WSGIApplication([
    ('/course/create', createCourse),
], debug=True)