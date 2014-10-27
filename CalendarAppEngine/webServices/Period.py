from google.appengine.ext import ndb

# Author : gilbrechbuhler
# Represents a Period of a course
class Period(ndb.Model):
    date = ndb.DateProperty()
    startTime = ndb.TimeProperty()
    endTime = ndb.TimeProperty()
    periodType = ndb.StringProperty()
    rooms = ndb.StringProperty(repeated=True)

    # Return the object fields in a JSON structure
    def get_as_json(self):
        obj = {
            'date' : self.date.strftime('%d/%m/%Y'),
            'startTime' : self.startTime.strftime('%H:%M'),
            'endTime' : self.endTime.strftime('%H:%M'),
            'periodType' : self.periodType,
            #'rooms' : self.rooms
        }
        return obj

    @classmethod
    def query_by_parent(cls, parent):
        return cls.query(ancestor=parent)