import datetime

# LOG_LEVEL = 'DEBUG' # Possible inputs: DEBUG, INFO, WARNING
LOG_LEVEL = 'INFO' # Possible inputs: DEBUG, INFO, WARNING

class Logging:
    @staticmethod
    def log(s, level):
        timestamp = datetime.datetime.now()
        if LOG_LEVEL == 'DEBUG':
            if level in ['DEBUG', 'SUPERINFO', 'INFO', 'WARNING']:
                print("{} | {} | {}".format(level, timestamp, s))
        elif LOG_LEVEL == 'INFO':
            if level in ['SUPERINFO', 'INFO', 'WARNING']:
                print("{} | {} | {}".format(level, timestamp, s))
        elif LOG_LEVEL == 'SUPERINFO':
            if level in ['SUPERINFO', 'WARNING']:
                print("{}".format(s))
        elif LOG_LEVEL == 'WARNING':
            if level in ['WARNING']:
                print("{} | {} | {}".format(level, timestamp, s))

    @staticmethod
    def debug(s):
        Logging.log(s, 'DEBUG')

    @staticmethod
    def info(s):
        Logging.log(s, 'INFO')

    @staticmethod
    def superInfo(s):
        Logging.log(s, 'SUPERINFO')

    @staticmethod
    def warning(s):
        Logging.log(s, 'WARNING')
