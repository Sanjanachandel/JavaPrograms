import os
import re

plugin_xml = """
            <plugin>
                <groupId>org.jacoco</groupId>
                <artifactId>jacoco-maven-plugin</artifactId>
                <version>0.8.11</version>
                <executions>
                    <execution>
                        <goals>
                            <goal>prepare-agent</goal>
                        </goals>
                    </execution>
                    <execution>
                        <id>report</id>
                        <phase>test</phase>
                        <goals>
                            <goal>report</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
"""

poms = []
for root, dirs, files in os.walk('.'):
    if 'target' in root:
        continue
    if 'pom.xml' in files:
        poms.append(os.path.join(root, 'pom.xml'))

for pom in poms:
    with open(pom, 'r', encoding='utf-8') as f:
        content = f.read()
        
    if 'jacoco-maven-plugin' not in content:
        # insert before the first </plugins>
        content = content.replace('</plugins>', plugin_xml + '        </plugins>', 1)
        with open(pom, 'w', encoding='utf-8') as f:
            f.write(content)
        print(f"Updated {pom}")
    else:
        print(f"Skipped {pom} (already has jacoco)")
