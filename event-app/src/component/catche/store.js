const TOKEN_KEY = 'token';
const ROLE_KEY = 'role';
const EMAIL_KEY = 'email';
const VENUES_KEY = 'venue_list';

export default {
  setEmail(email) {
    localStorage.setItem(EMAIL_KEY, email);
  },
  getEmail() {
    return localStorage.getItem(EMAIL_KEY) || null;
  },
  removeEmail() {
    localStorage.removeItem(EMAIL_KEY);
  },
  setToken(token) {
    localStorage.setItem(TOKEN_KEY, token);
  },
  getToken() {
    return localStorage.getItem(TOKEN_KEY) || null;
  },
  removeToken() {
    localStorage.removeItem(TOKEN_KEY);
  },
  setRole(role) {
    localStorage.setItem(ROLE_KEY, role);
  },
  getRole() {
    return localStorage.getItem(ROLE_KEY) || 'user';
  },
  removeRole() {
    localStorage.removeItem(ROLE_KEY);
  },

  setVenues(venues) {
    localStorage.setItem(VENUES_KEY, JSON.stringify(venues)); // Ensure stringifying here
  },
  getVenues() {
    const storedVenues = localStorage.getItem(VENUES_KEY);
    return storedVenues ? JSON.parse(storedVenues) : [];
  },
  getVenueByID(venueID) {
    const venues = this.getVenues();
    return venues.find(venue => venue.id === venueID) || null;
  },
  removeVenues() {
    localStorage.removeItem(VENUES_KEY);
  }
};
