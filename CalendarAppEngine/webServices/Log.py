from google.appengine.ext import ndb

from Period import Period

# Author : gilbrechbuhler
# Represents a Course in the DB
class Log(ndb.Model):
    logText = ndb.StringProperty()