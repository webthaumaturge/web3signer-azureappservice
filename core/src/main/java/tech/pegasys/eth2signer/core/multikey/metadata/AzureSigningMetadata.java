/*
 * Copyright 2020 ConsenSys AG.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package tech.pegasys.eth2signer.core.multikey.metadata;

import tech.pegasys.eth2signer.core.signing.ArtifactSigner;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AzureSigningMetadata implements SigningMetadata {

  private final String clientId;
  private final String clientSecret;
  private final String tenantId;
  private final String vaultName;
  private final String secretName;

  @JsonCreator
  public AzureSigningMetadata(
      @JsonProperty("clientId") final String clientId,
      @JsonProperty("clientSecret") final String clientSecret,
      @JsonProperty("tenantId") final String tenantId,
      @JsonProperty("vaultName") final String vaultName,
      @JsonProperty("secretName") final String secretName) {
    this.clientId = clientId;
    this.clientSecret = clientSecret;
    this.tenantId = tenantId;
    this.vaultName = vaultName;
    this.secretName = secretName;
  }

  public String getClientId() {
    return clientId;
  }

  public String getClientSecret() {
    return clientSecret;
  }

  public String getTenantId() {
    return tenantId;
  }

  public String getVaultName() {
    return vaultName;
  }

  public String getSecretName() {
    return secretName;
  }

  @Override
  public ArtifactSigner createSigner(final ArtifactSignerFactory factory) {
    return factory.create(this);
  }
}
