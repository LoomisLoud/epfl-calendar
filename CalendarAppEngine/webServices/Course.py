from google.appengine.ext import ndb

from Period import Period

# Author : gilbrechbuhler
# Represents a Course in the DB
class Course(ndb.Model):
    name = ndb.StringProperty(indexed=False)
    lowerCaseName = ndb.ComputedProperty(lambda self: self.name.lower())
    periodsKeys = ndb.KeyProperty(repeated=True, indexed=False)
    code = ndb.StringProperty()
    description = ndb.TextProperty(indexed=False)
    numberOfCredits = ndb.IntegerProperty()
    professorName = ndb.StringProperty()

    # Return the object's fields in a JSON structure
    def get_as_json(self):
        obj = {
            'name' : self.name,
            'code' : str(self.code),
            'description' : self.description,
            'numberOfCredits' : self.numberOfCredits,
            'professorName' : self.professorName
        }
        return obj

    @classmethod
    def query_by_code(cls, code):
        return cls.query().filter(ndb.GenericProperty("code") == code)

    @classmethod
    def query_by_name(cls, name):
        return cls.query().filter(ndb.GenericProperty("lowerCaseName") == name)

    @classmethod
    def isCodeUnique(self, code):
        query = self.query().filter(ndb.GenericProperty("code") == code)
        entity = query.get()
        if entity:
            return False
        return True