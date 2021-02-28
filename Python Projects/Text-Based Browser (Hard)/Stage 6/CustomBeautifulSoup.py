from abc import ABC
from bs4 import BeautifulSoup
from bs4.element import NavigableString, CData, Tag


class CustomBeautifulSoup(BeautifulSoup, ABC):
    """
    Custom implementation of BeautifulSoup to help indicate where to color in link text blue.
    Based on this StackOverFlow comment: https://stackoverflow.com/a/52026908
    """
    def _all_strings(self, strip=False, types=(NavigableString, CData)):
        for descendant in self.descendants:
            # return "a" string representation if we encounter it
            if isinstance(descendant, Tag) and descendant.name == 'a' and descendant.string:
                yield "CUSTOM_PRINT_BLUE" + str(descendant.string)

            # skip an inner text node inside "a"
            if isinstance(descendant, NavigableString) and descendant.parent.name == 'a':
                continue

            # default behavior
            if (types is None and not isinstance(descendant, NavigableString)) \
                    or (types is not None and type(descendant) not in types):
                continue

            if strip:
                descendant = descendant.strip()
                if len(descendant) == 0:
                    continue
            yield descendant
