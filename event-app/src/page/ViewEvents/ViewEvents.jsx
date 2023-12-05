import React, { useEffect, useState } from 'react';
import {
  Button,
  Card,
  CardContent,
  CardHeader,
  Container,
  Grid,
  Typography,
  CardMedia,
  TextField, // Import TextField for the search input
} from '@mui/material';
import { useHistory } from 'react-router-dom';
import './ViewEvents.css';
import store from '../../component/catche/store';

function ViewEvents() {
  const [events, setEvents] = useState([]);
  const [searchInput, setSearchInput] = useState(''); // State to store the search input
  const userRole = store.getRole();
  const token = store.getToken();
  const history = useHistory();
  useEffect(() => {
    // Fetch events data from the API with the token in the header
    fetch(
      `${process.env.REACT_APP_API_URL}/view-event?mode=all`, {
      method: 'GET',
      headers: {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json',
      },
    })
      .then((response) => {
        if (response.status === 200) {
          return response.json();
        } else {
          throw new Error('Unauthorized');
        }
      })
      .then((data) => setEvents(data))
      .catch((error) => console.error('Error fetching events:', error));
  }, [token]);

  const onClickBuyTicket = async (id) => {
    history.push(`/bookTicket/${id}`);
  };

  const onClickCancelEvent = async (id) => {
    try {
      const response = await fetch(`http://localhost:8080//delete-event?id=${id}`, {
        method: 'DELETE',
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json',
        },
      });

      if (response.status === 200) {
        console.log('Event canceled successfully');
        // After canceling the event, fetch the updated list of events
        fetch(`http://localhost:8080/events`, {
          method: 'GET',
          headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json',
          },
        })
          .then((response) => {
            if (response.status === 200) {
              return response.json();
            } else {
              throw new Error('Unauthorized');
            }
          })
          .then((data) => setEvents(data))
          .catch((error) => console.error('Error fetching events:', error));
      } else {
        console.error('Failed to cancel event');
      }
    } catch (error) {
      console.error('An error occurred while canceling event', error);
    }
  };

  // Function to handle search input change
  const handleSearchInputChange = (e) => {
    setSearchInput(e.target.value);
  };

  // Function to handle search button click
  const handleSearch = () => {
    // Make a GET request with the search input as a URL parameter
    fetch(`http://localhost:8080/view-event?mode=search&input=${searchInput}`, {
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

  return (
    <Container maxWidth="xl">
      <Typography variant="h4" sx={{ mt: 3, mb: 2 }}>
        View Events
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
              <CardHeader title={event.name} subheader={event.date} />
              <CardMedia
                component="img"
                height="300"
                image={event.imageUrl}
                alt={event.name}
              />
              <CardContent>
                <Typography variant="body2" color="text.secondary">
                  Venue: {event.venue.name}
                  <br />
                  Description: {event.description}
                </Typography>
                <div className='button-section'>
                  {userRole === '1' ? (
                    <Button
                      variant="contained"
                      color="primary"
                      onClick={() => onClickCancelEvent(event.id)}
                    >
                      Cancel
                    </Button>
                  ) : (
                    <Button
                      variant="contained"
                      color="primary"
                      onClick={() => onClickBuyTicket(event.id)}
                    >
                      Buy
                    </Button>
                  )}
                </div>
              </CardContent>
            </Card>
          </Grid>
        ))}
      </Grid>
    </Container>
  );
}

export default ViewEvents;

