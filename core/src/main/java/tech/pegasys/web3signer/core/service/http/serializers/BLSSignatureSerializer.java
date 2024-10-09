/*
 * Copyright 2024 ConsenSys AG.
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
package tech.pegasys.web3signer.core.service.http.serializers;

import tech.pegasys.teku.api.schema.BLSSignature;

import java.io.IOException;
import java.util.Locale;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class BLSSignatureSerializer extends JsonSerializer<BLSSignature> {
  public BLSSignatureSerializer() {}

  @Override
  public void serialize(BLSSignature value, JsonGenerator gen, SerializerProvider serializers)
      throws IOException {
    gen.writeString(value.toHexString().toLowerCase(Locale.ROOT));
  }
}
