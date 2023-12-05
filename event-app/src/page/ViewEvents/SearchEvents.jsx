import React, { useEffect, useState } from 'react';
import { TextField, Button, Card, CardContent, CardHeader, Container, Grid, Typography,
} from '@mui/material';
import { useHistory } from 'react-router-dom';
import './ViewEvents.css';
import store from '../../component/catche/store';

const MockEvents = [

  {
    id: "1",
    name: 'Event1',
    startDate: '2023-10-25 12:00',
    endDate: '2023-10-25 15:00',
    planners: ["p1@email.com", "p2@email.com"],
    venue: {
      id: "v1",
      name: "vname",
      address: "vaddress",
      sections: [ { id: "s1", type: "seated", capacity: 100, price: 20, ticketLeft: 20 } ]
    },
    artist: "artist1",
    description: 'This is Event 1',
  },
];

function SearchEvents() {
  const [events, setEvents] = useState([]);
  const [searchInput, setSearchInput] = useState(''); // State to store the search input
  const token = store.getToken();
  const history = useHistory();

  // Function to handle search input change
  const handleSearchInputChange = (e) => {
    setSearchInput(e.target.value);
  };

  // Function to handle search button click
  const handleSearch = () => {
    // Make a GET request with the search input as a URL parameter
    fetch(`${process.env.REACT_APP_API_URL}/view-event?mode=search&input=${searchInput}`, {
      method: 'GET',
      headers: {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json',
      },
    })
      .then((response) => {
        if (response.status === 200) {
          return response.json();
        } else if (response.status === 404) {
          throw new Error('System error');
        } else {
          throw new Error('Unauthorized');
        }
      })
      .then((data) => setEvents(data))
      .catch((error) => console.error('Error searching events:', error));
  };

  const onClickBookTickets = (event) => history.push({
    pathname: `/bookTicket/${event.id}`,
    state: { event: event }
  })

  return (
    <Container maxWidth="xl">
      <Typography variant="h4" sx={{ mt: 3, mb: 2 }}>
        Search Events
      </Typography>
      <div className="search-bar" >
        <TextField
          label="Search Event"
          value={searchInput}
          onChange={handleSearchInputChange}
          variant="outlined"
          fullWidth
          sx={{ marginRight: '8px' }} // Add margin to separate the input and button
        />
        <Button
          variant="contained"
          color="primary"
          size='large'
          onClick={handleSearch}
        >
          Search
        </Button>
      </div>
      <Grid container spacing={3}>
        {events.map((event) => (
          <Grid key={event.id} item xs={12} sm={6} md={4}>
            <Card>
              <CardHeader title={event.name} subheader={`${event.startDate} ~ ${event.endDate}`} />
              <CardContent>
                <Typography variant="body2" color="text.secondary">
                  Venue: {event.venue.name}<br />
                  Address: {event.venue.address}<br />
                  Artist: {event.artist}<br />
                  Description: {event.description}<br />
                  {/* Sections: {event.venue.sections.map(s => {
                    return <p>- {s.type} (${s.price}): {s.ticketLeft}/{s.capacity}</p>
                  })} */}
                </Typography>
                <div className='button-section'>
                  <Button variant="contained" color="primary"
                    onClick={() => onClickBookTickets(event)}
                  >
                    Book Tickets
                  </Button>
                </div>
              </CardContent>
            </Card>
          </Grid>
        ))}
      </Grid>
    </Container>
  );
}

export default SearchEvents;

