package com.pallas.memberservice.domain;

/** Profile data returned by the identity provider for a given subject claim. */
public record IdentityProfile(String firstName, String lastName) {}
