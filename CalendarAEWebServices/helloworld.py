import webapp2
from google.appengine.ext import ndb
import CourseMatching
import constants

class MainPage(webapp2.RequestHandler):
    def get(self):
        self.response.headers['Content-Type'] = 'text/plain'
        code = self.request.get('code')
        ancestor_key = ndb.Key(constants.COURSE_LIST_ANCESTOR_NAME, constants.COURSE_LIST_ANCESTOR_ID)
        cm = CourseMatching.CourseMatching.query_by_code(ancestor_key, code).fetch(20)
        self.response.out.write(len(cm))
        for c in cm:
            self.response.out.write(c.name)

    def post(self):
        # We set the parent key on each 'Greeting' to ensure each guestbook's
        # greetings are in the same entity group.
        course_matching = CourseMatching.CourseMatching(parent=ndb.Key(constants.COURSE_LIST_ANCESTOR_NAME, constants.COURSE_LIST_ANCESTOR_ID),
                        code = self.request.get('code'), name = self.request.get('name'))
        course_matching.put()
        #self.redirect('/?' + urllib.urlencode({'guestbook_name': guestbook_name}))


application = webapp2.WSGIApplication([
    ('/', MainPage),
], debug=True)

