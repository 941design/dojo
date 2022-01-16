class FastaParser(object):
    
    open_symbols = '>'
    
    def __init__(self, file):
        self.file = file
        self.buffer = None

    def parseTuples(self):
        """ Returns list of tuples
        """
        fn_add = lambda xs, kv : xs.append(kv,)
        return self._parse([], fn_add)

    def parseDict(self):
        """ Returns dictionary
        """
        fn_add = lambda d, kv : d.update({kv[0]: kv[1]})
        return self._parse({}, fn_add)

    def _parse(self, coll, fn_add):
        self._parseInto(coll, fn_add)
        return coll

    def _parseInto(self, coll, fn_add):
        tag = self._parseTag()
        # sucks, while does not take expression as condition!
        while tag:
            s = self._parseString()
            fn_add(coll, (tag, s))
            tag = self._parseTag()

    def _readline(self):
        return self.file.readline().strip()
        
    def _parseTag(self):
        if self.buffer:
            tag = self.buffer
        else:
            tag = self._readline()
        if not tag:
            return None
        elif tag[0] in self.open_symbols:
            return tag[1:]
        else: 
            self._error('malformatted string')

    def _parseString(self):
        s = ''
        self.buffer = self._readline()
        while self.buffer and not self.buffer[0] in self.open_symbols:
            s += self.buffer
            self.buffer = self._readline()
        return s

    def _errror(self, comment):
        raise SyntaxError(comment)
