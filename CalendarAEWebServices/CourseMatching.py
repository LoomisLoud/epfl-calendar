from google.appengine.ext import ndb

class CourseMatching(ndb.Model):
    code = ndb.StringProperty()
    name = ndb.StringProperty()

    @classmethod
    def query_by_code(cls, ancestor_key, code):
        return cls.query(ancestor=ancestor_key).filter(ndb.GenericProperty("code") == code)