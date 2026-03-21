# Song A Day
## Overview
Spring Boot REST API for an application where you can log your song of the day. 
Handles authentication, user data management, and integration with Spotify APIs for track metadata.

## Tech Stack
Backend: Java, Spring Boot  
Security: Spring Security (OAuth2 [Spotify] + JWT)  
Database: PostgreSQL  
Caching: Redis (cache-aside pattern)  
Build Tool: Maven  

## Additional design patterns:
- DTO-based request/response handling  
- External API integration (Spotify Web API)  
- Redis caching for performance optimization

## Caching Strategy
- Uses Redis to cache Spotify track metadata
- Implements cache-aside pattern
- Reduces redundant external API calls & improves response latency

## Authentication Flow
Authentication is implemented using Spotify OAuth combined with JWT.

1. User logs in via Spotify OAuth
2. Spotify's access token and refresh token for the user are persisted in database
3. Backend generates access token and refresh token (JWT)
4. Tokens are stored in HTTP-only cookies
5. Access token is used for authenticated API calls
6. On expiry, client calls refresh endpoint to obtain a new access token
7. If refresh token expires, user is logged out

## Future improvements
- Deployment!
- Implementing pagination to read song history
- Setting song entry / memory as private
- Generating playlist for a week
- Viewing other people's profiles
- Storing history for song entries of a day
