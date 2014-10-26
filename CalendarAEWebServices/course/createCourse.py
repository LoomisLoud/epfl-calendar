import webapp2
from google.appengine.ext import ndb

import Course

# Author : gilbrechbuhler
# Web service used to create a course entity in the database
class createCourse(webapp2.RequestHandler):
    def post(self):
        codeGet = self.request.get('code')
        if not(Course.Course.isCodeUnique(codeGet)):
            self.response.headers['Content-Type'] = 'text/plain'
            self.response.write('error')
        else:
            nameGet = self.request.get('name')
            descriptionGet = self.request.get('description')
            numberOfCreditsGet = self.request.get('numberOfCredits')
            professorNameGet = self.request.get('professorName')
            course = Course.Course(name = nameGet, code = codeGet, description = descriptionGet, 
                numberOfCredits = int(numberOfCreditsGet), professorName = professorNameGet)
            course.put()


application = webapp2.WSGIApplication([
    ('/createCourse', createCourse),
], debug=True)